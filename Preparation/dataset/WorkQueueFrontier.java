/* Copyright (C) 2009 Internet Archive
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Heritrix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Heritrix; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
  */
package org.archive.crawler.frontier;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.BagUtils;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.lang.StringUtils;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CoreAttributeConstants;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.datamodel.UriUniqFilter;
import org.archive.crawler.datamodel.UriUniqFilter.HasUriReceiver;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.framework.Frontier;
import org.archive.crawler.framework.exceptions.EndedException;
import org.archive.crawler.framework.exceptions.FatalConfigurationException;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;
import org.archive.net.UURI;
import org.archive.util.ArchiveUtils;
import org.archive.util.ObjectIdentityCache;
import org.archive.util.ObjectIdentityMemCache;

import com.sleepycat.collections.StoredIterator;

/**
 * A common Frontier base using several queues to hold pending URIs. 
 * 
 * Uses in-memory map of all known 'queues' inside a single database.
 * Round-robins between all queues.
 *
 * @author Gordon Mohr
 * @author Christian Kohlschuetter
 */
public abstract class WorkQueueFrontier extends AbstractFrontier
implements FetchStatusCodes, CoreAttributeConstants, HasUriReceiver,
        Serializable {
	private static final long serialVersionUID = 570384305871965843L;
	
    public class WakeTask extends TimerTask {
        @Override
        public void run() {
            synchronized(snoozedClassQueues) {
                if(this!=nextWake) {
                    // an intervening waketask was made
                    return;
                }
                wakeQueues();
            }
        }
    }

    /** truncate reporting of queues at some large but not unbounded number */
    private static final int REPORT_MAX_QUEUES = 2000;
    
    /**
     * If we know that only a small amount of queues is held in memory,
     * we can avoid using a disk-based BigMap.
     * This only works efficiently if the WorkQueue does not hold its
     * entries in memory as well.
     */ 
    private static final int MAX_QUEUES_TO_HOLD_ALLQUEUES_IN_MEMORY = 3000;

    /**
     * When a snooze target for a queue is longer than this amount, and 
     * there are already ready queues, deactivate rather than snooze 
     * the current queue -- so other more responsive sites get a chance
     * in active rotation. (As a result, queue's next try may be much
     * further in the future than the snooze target delay.)
     */
    public final static String ATTR_SNOOZE_DEACTIVATE_MS =
        "snooze-deactivate-ms";
    public static Long DEFAULT_SNOOZE_DEACTIVATE_MS = new Long(5*60*1000); // 5 minutes
    
    private static final Logger logger =
        Logger.getLogger(WorkQueueFrontier.class.getName());
    
    /** whether to hold queues INACTIVE until needed for throughput */
    public final static String ATTR_HOLD_QUEUES = "hold-queues";
    protected final static Boolean DEFAULT_HOLD_QUEUES = new Boolean(true); 

    /** amount to replenish budget on each activation (duty cycle) */
    public final static String ATTR_BALANCE_REPLENISH_AMOUNT =
        "balance-replenish-amount";
    protected final static Integer DEFAULT_BALANCE_REPLENISH_AMOUNT =
        new Integer(3000);
    
    /** whether to hold queues INACTIVE until needed for throughput */
    public final static String ATTR_ERROR_PENALTY_AMOUNT =
        "error-penalty-amount";
    protected final static Integer DEFAULT_ERROR_PENALTY_AMOUNT =
        new Integer(100);


    /** total expenditure to allow a queue before 'retiring' it  */
    public final static String ATTR_QUEUE_TOTAL_BUDGET = "queue-total-budget";
    protected final static Long DEFAULT_QUEUE_TOTAL_BUDGET = new Long(-1);

    /** cost assignment policy to use (by class name) */
    public final static String ATTR_COST_POLICY = "cost-policy";
    protected final static String DEFAULT_COST_POLICY =
        UnitCostAssignmentPolicy.class.getName();

    /** target size of ready queues backlog */
    public final static String ATTR_TARGET_READY_QUEUES_BACKLOG =
        "target-ready-backlog";
    protected final static Integer DEFAULT_TARGET_READY_QUEUES_BACKLOG =
        new Integer(50);
    
    /** those UURIs which are already in-process (or processed), and
     thus should not be rescheduled */
    protected transient UriUniqFilter alreadyIncluded;

    /** All known queues.
     */
    protected transient ObjectIdentityCache<String,WorkQueue> allQueues = null; 
    // of classKey -> ClassKeyQueue

    /**
     * All per-class queues whose first item may be handed out.
     * Linked-list of keys for the queues.
     */
    protected BlockingQueue<String> readyClassQueues;
    
    /** Target (minimum) size to keep readyClassQueues */
    protected int targetSizeForReadyQueues;
    
    /** single-thread access to ready-filling code */
    protected transient Semaphore readyFiller = new Semaphore(1);
    
    /** 
     * All 'inactive' queues, not yet in active rotation.
     * Linked-list of keys for the queues.
     */
    protected Queue<String> inactiveQueues;

    /**
     * 'retired' queues, no longer considered for activation.
     * Linked-list of keys for queues.
     */
    protected Queue<String> retiredQueues;
    
    /** all per-class queues from whom a URI is outstanding */
    protected Bag inProcessQueues = 
        BagUtils.synchronizedBag(new HashBag()); // of ClassKeyQueue
    
    /**
     * All per-class queues held in snoozed state, sorted by wake time.
     */
    protected SortedSet<WorkQueue> snoozedClassQueues;
    
    /** Timer for tasks which wake head item of snoozedClassQueues */
    protected transient Timer wakeTimer;
    
    /** Task for next wake */ 
    protected transient WakeTask nextWake; 
    
    protected WorkQueue longestActiveQueue = null;
    
    /** how long to wait for a ready queue when there's nothing snoozed */
    private static final long DEFAULT_WAIT = 1000; // 1 second

    /** a policy for assigning 'cost' values to CrawlURIs */
    private transient CostAssignmentPolicy costAssignmentPolicy;
    
    /** all policies available to be chosen */
    String[] AVAILABLE_COST_POLICIES = new String[] {
            ZeroCostAssignmentPolicy.class.getName(),
            UnitCostAssignmentPolicy.class.getName(),
            WagCostAssignmentPolicy.class.getName(),
            AntiCalendarCostAssignmentPolicy.class.getName()};

    /**
     * Create the CommonFrontier
     * 
     * @param name
     * @param description
     */
    public WorkQueueFrontier(String name, String description) {
        // The 'name' of all frontiers should be the same (URIFrontier.ATTR_NAME)
        // therefore we'll ignore the supplied parameter.
        super(Frontier.ATTR_NAME, description);
        Type t = addElementToDefinition(new SimpleType(ATTR_HOLD_QUEUES,
            "Whether to hold newly-created per-host URI work" +
            " queues until needed to stay busy. If false (default)," +
            " all queues may contribute URIs for crawling at all" +
            " times. If true, queues begin (and collect URIs) in" +
            " an 'inactive' state, and only when the Frontier needs" +
            " another queue to keep all ToeThreads busy will new" +
            " queues be activated.", DEFAULT_HOLD_QUEUES));
        t.setExpertSetting(true);
        t.setOverrideable(false);
        t = addElementToDefinition(new SimpleType(ATTR_BALANCE_REPLENISH_AMOUNT,
            "Amount to replenish a queue's activity balance when it becomes " +
            "active. Larger amounts mean more URIs will be tried from the " +
            "queue before it is deactivated in favor of waiting queues. " +
            "Default is 3000", DEFAULT_BALANCE_REPLENISH_AMOUNT));
        t.setExpertSetting(true);
        t.setOverrideable(true);
        t = addElementToDefinition(new SimpleType(ATTR_ERROR_PENALTY_AMOUNT,
                "Amount to additionally penalize a queue when one of" +
                "its URIs fails completely. Accelerates deactivation or " +
                "full retirement of problem queues and unresponsive sites. " +
                "Default is 100", DEFAULT_ERROR_PENALTY_AMOUNT));
        t.setExpertSetting(true);
        t.setOverrideable(true);
        t = addElementToDefinition(new SimpleType(ATTR_QUEUE_TOTAL_BUDGET,
            "Total activity expenditure allowable to a single queue; queues " +
            "over this expenditure will be 'retired' and crawled no more. " +
            "Default of -1 means no ceiling on activity expenditures is " +
            "enforced.", DEFAULT_QUEUE_TOTAL_BUDGET));
        t.setExpertSetting(true);
        t.setOverrideable(true);

        t = addElementToDefinition(new SimpleType(ATTR_COST_POLICY,
                "Policy for calculating the cost of each URI attempted. " +
                "The default UnitCostAssignmentPolicy considers the cost of " +
                "each URI to be '1'.", DEFAULT_COST_POLICY, AVAILABLE_COST_POLICIES));
        t.setExpertSetting(true);
        
        t = addElementToDefinition(new SimpleType(ATTR_SNOOZE_DEACTIVATE_MS,
                "Threshold above which any 'snooze' delay will cause the " +
                "affected queue to go inactive, allowing other queues a " +
                "chance to rotate into active state. Typically set to be " +
                "longer than the politeness pauses between successful " +
                "fetches, but shorter than the connection-failed " +
                "'retry-delay-seconds'. (Default is 5 minutes.)", 
                DEFAULT_SNOOZE_DEACTIVATE_MS));
        t.setExpertSetting(true);
        t.setOverrideable(false);
        t = addElementToDefinition(new SimpleType(ATTR_TARGET_READY_QUEUES_BACKLOG,
                "Target size for backlog of ready queues. This many queues " +
                "will be brought into 'ready' state even if a thread is " +
                "not waiting. Only has effect if 'hold-queues' is true. " +
                "Default is 50.", DEFAULT_TARGET_READY_QUEUES_BACKLOG));
        t.setExpertSetting(true);
        t.setOverrideable(false);
    }

    /**
     * Initializes the Frontier, given the supplied CrawlController.
     *
     * @see org.archive.crawler.framework.Frontier#initialize(org.archive.crawler.framework.CrawlController)
     */
    public void initialize(CrawlController c)
            throws FatalConfigurationException, IOException {
        // Call the super method. It sets up frontier journalling.
        super.initialize(c);
        this.controller = c;
        
        initQueuesOfQueues();
        
        this.targetSizeForReadyQueues = (Integer)getUncheckedAttribute(null,
            ATTR_TARGET_READY_QUEUES_BACKLOG);
        if (this.targetSizeForReadyQueues < 1) {
            this.targetSizeForReadyQueues = 1;
        }
        this.wakeTimer = new Timer("waker for " + c.toString());
        
        try {
            if (workQueueDataOnDisk()
                    && getQueueAssignmentPolicy(null).maximumNumberOfKeys() >= 0
                    && getQueueAssignmentPolicy(null).maximumNumberOfKeys() <= 
                        MAX_QUEUES_TO_HOLD_ALLQUEUES_IN_MEMORY) {
                this.allQueues = new ObjectIdentityMemCache<WorkQueue>(701, .9f, 100);
            } else {
                this.allQueues = c.getBigMap("allqueues", WorkQueue.class);
                if (logger.isLoggable(Level.FINE)) {
                    Iterator<String> i = this.allQueues.keySet().iterator();
                    try {
                        for (; i.hasNext();) {
                            logger.fine((String) i.next());
                        }
                    } finally {
                        StoredIterator.close(i);
                    }
                }
            }
            this.alreadyIncluded = createAlreadyIncluded();
            initQueue();
        } catch (IOException e) {
            e.printStackTrace();
            throw (FatalConfigurationException)
                new FatalConfigurationException(e.getMessage()).initCause(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw (FatalConfigurationException)
                new FatalConfigurationException(e.getMessage()).initCause(e);
        }
        
        initCostPolicy();
        
        loadSeeds();
    }
    
    /**
     * Set up the various queues-of-queues used by the frontier. Override
     * in implementing subclasses to reduce or eliminate risk of queues
     * growing without bound. 
     */
    protected void initQueuesOfQueues() {
        // small risk of OutOfMemoryError: if 'hold-queues' is false,
        // readyClassQueues may grow in size without bound
        readyClassQueues = new LinkedBlockingQueue<String>();
        // risk of OutOfMemoryError: in large crawls, 
        // inactiveQueues may grow in size without bound
        inactiveQueues = new LinkedBlockingQueue<String>();
        // risk of OutOfMemoryError: in large crawls with queue max-budgets, 
        // inactiveQueues may grow in size without bound
        retiredQueues = new LinkedBlockingQueue<String>();
        // small risk of OutOfMemoryError: in large crawls with many 
        // unresponsive queues, an unbounded number of snoozed queues 
        // may exist
        snoozedClassQueues = Collections.synchronizedSortedSet(new TreeSet<WorkQueue>());
    }

    /**
     * Set (or reset after configuration change) the cost policy in effect.
     * 
     * @throws FatalConfigurationException
     */
    private void initCostPolicy() throws FatalConfigurationException {
        try {
            costAssignmentPolicy = (CostAssignmentPolicy) Class.forName(
                    (String) getUncheckedAttribute(null, ATTR_COST_POLICY))
                    .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FatalConfigurationException(e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.frontier.AbstractFrontier#crawlEnded(java.lang.String)
     */
    public void crawlEnded(String sExitMessage) {
        // Cleanup.  CrawlJobs persist after crawl has finished so undo any
        // references.
        if (this.alreadyIncluded != null) {
            this.alreadyIncluded.close();
            this.alreadyIncluded = null;
        }
        
        try {
            closeQueue();
        } catch (IOException e) {
            // FIXME exception handling
            e.printStackTrace();
        }
        this.wakeTimer.cancel();
        
        this.allQueues.close();
        this.allQueues = null;
        this.inProcessQueues = null;
        this.readyClassQueues = null;
        this.snoozedClassQueues = null;
        this.inactiveQueues = null;
        this.retiredQueues = null;
        
        this.costAssignmentPolicy = null;
        
        // Clearing controller is a problem. We get NPEs in #preNext.
        super.crawlEnded(sExitMessage);
        this.controller = null;
    }

    /**
     * Create a UriUniqFilter that will serve as record 
     * of already seen URIs.
     *
     * @return A UURISet that will serve as a record of already seen URIs
     * @throws IOException
     */
    protected abstract UriUniqFilter createAlreadyIncluded() throws IOException;

    /**
     * Arrange for the given CandidateURI to be visited, if it is not
     * already scheduled/completed.
     *
     * @see org.archive.crawler.framework.Frontier#schedule(org.archive.crawler.datamodel.CandidateURI)
     */
    public void schedule(CandidateURI caUri) {
        // Canonicalization may set forceFetch flag.  See
        // #canonicalization(CandidateURI) javadoc for circumstance.
        String canon = canonicalize(caUri);
        if (caUri.forceFetch()) {
            alreadyIncluded.addForce(canon, caUri);
        } else {
            alreadyIncluded.add(canon, caUri);
        }
    }

    /**
     * Accept the given CandidateURI for scheduling, as it has
     * passed the alreadyIncluded filter. 
     * 
     * Choose a per-classKey queue and enqueue it. If this
     * item has made an unready queue ready, place that 
     * queue on the readyClassQueues queue. 
     * @param caUri CandidateURI.
     */
    public void receive(CandidateURI caUri) {
        CrawlURI curi = asCrawlUri(caUri);
        applySpecialHandling(curi);
        sendToQueue(curi);
        // Update recovery log.
        doJournalAdded(curi);
    }

	/* (non-Javadoc)
	 * @see org.archive.crawler.frontier.AbstractFrontier#asCrawlUri(org.archive.crawler.datamodel.CandidateURI)
	 */
	protected CrawlURI asCrawlUri(CandidateURI caUri) {
		CrawlURI curi = super.asCrawlUri(caUri);
		// force cost to be calculated, pre-insert
		getCost(curi);
		return curi;
	}
	
    /**
     * Send a CrawlURI to the appropriate subqueue.
     * 
     * @param curi
     */
    protected void sendToQueue(CrawlURI curi) {
        WorkQueue wq = getQueueFor(curi);
        synchronized (wq) {
            wq.enqueue(this, curi);
            if(!wq.isRetired()) {
                incrementQueuedUriCount();
            }
            if(!wq.isHeld()) {
                wq.setHeld();
                if(holdQueues() && readyClassQueues.size()>=targetSizeForReadyQueues()) {
                    deactivateQueue(wq);
                } else {
                    replenishSessionBalance(wq);
                    readyQueue(wq);
                }
            }
            WorkQueue laq = longestActiveQueue;
            if(!wq.isRetired()&&((laq==null) || wq.getCount() > laq.getCount())) {
                longestActiveQueue = wq; 
            }
        }
    }

    /**
     * Whether queues should start inactive (only becoming active when needed
     * to keep the crawler busy), or if queues should start out ready.
     * 
     * @return true if new queues should held inactive
     */
    private boolean holdQueues() {
        return ((Boolean) getUncheckedAttribute(null, ATTR_HOLD_QUEUES))
                .booleanValue();
    }

    /**
     * Put the given queue on the readyClassQueues queue
     * @param wq
     */
    private void readyQueue(WorkQueue wq) {
        try {
            wq.setActive(this, true);
            readyClassQueues.put(wq.getClassKey());
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("unable to ready queue "+wq);
            // propagate interrupt up 
            throw new RuntimeException(e);
        }
    }

    /**
     * Put the given queue on the inactiveQueues queue
     * @param wq
     */
    private void deactivateQueue(WorkQueue wq) {
//        try {
            wq.setSessionBalance(0); // zero out session balance
            inactiveQueues.add(wq.getClassKey());
            wq.setActive(this, false);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            System.err.println("unable to deactivate queue "+wq);
//            // propagate interrupt up 
//            throw new RuntimeException(e);
//        }
    }
    
    /**
     * Put the given queue on the retiredQueues queue
     * @param wq
     */
    private void retireQueue(WorkQueue wq) {
//        try {
            retiredQueues.add(wq.getClassKey());
            decrementQueuedCount(wq.getCount());
            wq.setRetired(true);
            wq.setActive(this, false);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            System.err.println("unable to retire queue "+wq);
//            // propagate interrupt up 
//            throw new RuntimeException(e);
//        }
    }
    
    /** 
     * Accomodate any changes in settings.
     * 
     * @see org.archive.crawler.framework.Frontier#kickUpdate()
     */
    public void kickUpdate() {
        super.kickUpdate();
        int target = (Integer)getUncheckedAttribute(null,
                ATTR_TARGET_READY_QUEUES_BACKLOG);
        if (target < 1) {
            target = 1;
        }
        this.targetSizeForReadyQueues = target; 
        try {
            initCostPolicy();
        } catch (FatalConfigurationException fce) {
            throw new RuntimeException(fce);
        }
        // The rules for a 'retired' queue may have changed; so,
        // unretire all queues to 'inactive'. If they still qualify
        // as retired/overbudget next time they come up, they'll
        // be re-retired; if not, they'll get a chance to become
        // active under the new rules.
        Object key = this.retiredQueues.poll();
        while (key != null) {
            WorkQueue q = (WorkQueue)this.allQueues.get((String)key);
            if(q != null) {
                unretireQueue(q);
            }
            key = this.retiredQueues.poll();
        }
    }
    /**
     * Restore a retired queue to the 'inactive' state. 
     * 
     * @param q
     */
    private void unretireQueue(WorkQueue q) {
        deactivateQueue(q);
        q.setRetired(false); 
        incrementQueuedUriCount(q.getCount());
    }

    /**
     * Return the work queue for the given CrawlURI's classKey. URIs
     * are ordered and politeness-delayed within their 'class'.
     * If the requested queue is not found, a new instance is created.
     * 
     * @param curi CrawlURI to base queue on
     * @return the found or created ClassKeyQueue
     */
    protected abstract WorkQueue getQueueFor(CrawlURI curi);

    /**
     * Return the work queue for the given classKey, or null
     * if no such queue exists.
     * 
     * @param classKey key to look for
     * @return the found WorkQueue
     */
    protected abstract WorkQueue getQueueFor(String classKey);
    
    /**
     * Return the next CrawlURI to be processed (and presumably
     * visited/fetched) by a a worker thread.
     *
     * Relies on the readyClassQueues having been loaded with
     * any work queues that are eligible to provide a URI. 
     *
     * @return next CrawlURI to be processed. Or null if none is available.
     *
     * @see org.archive.crawler.framework.Frontier#next()
     */
    public CrawlURI next()
    throws InterruptedException, EndedException {
        while (true) { // loop left only by explicit return or exception
            long now = System.currentTimeMillis();

            // Do common checks for pause, terminate, bandwidth-hold
            preNext(now);
            
            // allow up-to-1 thread to fill readyClassQueues to target
            if(readyFiller.tryAcquire()) {
                try {
                    int activationsNeeded = targetSizeForReadyQueues() - readyClassQueues.size();
                    while(activationsNeeded > 0 && !inactiveQueues.isEmpty()) {
                        activateInactiveQueue();
                        activationsNeeded--;
                    }
                } finally {
                    readyFiller.release();
                }
            }
                   
            WorkQueue readyQ = null;
            Object key = readyClassQueues.poll(DEFAULT_WAIT,TimeUnit.MILLISECONDS);
            if (key != null) {
                readyQ = (WorkQueue)this.allQueues.get((String)key);
            }
            if (readyQ != null) {
                while(true) { // loop left by explicit return or break on empty
                    CrawlURI curi = null;
                    synchronized(readyQ) {
                        curi = readyQ.peek(this);                     
                        if (curi != null) {
                            // check if curi belongs in different queue
                            String currentQueueKey = getClassKey(curi);
                            if (currentQueueKey.equals(curi.getClassKey())) {
                                // curi was in right queue, emit
                                noteAboutToEmit(curi, readyQ);
                                inProcessQueues.add(readyQ);
                                return curi;
                            }
                            // URI's assigned queue has changed since it
                            // was queued (eg because its IP has become
                            // known). Requeue to new queue.
                            curi.setClassKey(currentQueueKey);
                            readyQ.dequeue(this);
                            decrementQueuedCount(1);
                            curi.setHolderKey(null);
                            // curi will be requeued to true queue after lock
                            //  on readyQ is released, to prevent deadlock
                        } else {
                            // readyQ is empty and ready: it's exhausted
                            // release held status, allowing any subsequent 
                            // enqueues to again put queue in ready
                            readyQ.clearHeld();
                            break;
                        }
                    }
                    if(curi!=null) {
                        // complete the requeuing begun earlier
                        sendToQueue(curi);
                    }
                }
            } else {
                // ReadyQ key wasn't in all queues: unexpected
                if (key != null) {
                    logger.severe("Key "+ key +
                        " in readyClassQueues but not allQueues");
                }
            }

            if(shouldTerminate) {
                // skip subsequent steps if already on last legs
                throw new EndedException("shouldTerminate is true");
            }
                
            if(inProcessQueues.size()==0) {
                // Nothing was ready or in progress or imminent to wake; ensure 
                // any piled-up pending-scheduled URIs are considered
                this.alreadyIncluded.requestFlush();
            }    
        }
    }

    private int targetSizeForReadyQueues() {
        return targetSizeForReadyQueues;
    }

    /**
     * Return the 'cost' of a CrawlURI (how much of its associated
     * queue's budget it depletes upon attempted processing)
     * 
     * @param curi
     * @return the associated cost
     */
    private int getCost(CrawlURI curi) {
        int cost = curi.getHolderCost();
        if (cost == CrawlURI.UNCALCULATED) {
            cost = costAssignmentPolicy.costOf(curi);
            curi.setHolderCost(cost);
        }
        return cost;
    }
    
    /**
     * Activate an inactive queue, if any are available. 
     */
    private void activateInactiveQueue() {
        Object key = this.inactiveQueues.poll();
        if (key == null) {
            return;
        }
        WorkQueue candidateQ = (WorkQueue)this.allQueues.get((String)key);
        if(candidateQ != null) {
            synchronized(candidateQ) {
                replenishSessionBalance(candidateQ);
                if(candidateQ.isOverBudget()){
                    // if still over-budget after an activation & replenishing,
                    // retire
                    retireQueue(candidateQ);
                    return;
                } 
                long now = System.currentTimeMillis();
                long delay_ms = candidateQ.getWakeTime() - now;
                if(delay_ms>0) {
                    // queue still due for snoozing
                    snoozeQueue(candidateQ,now,delay_ms);
                    return;
                }
                candidateQ.setWakeTime(0); // clear obsolete wake time, if any
                readyQueue(candidateQ);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("ACTIVATED queue: " +
                        candidateQ.getClassKey());
                   
                }
            }
        }
    }

    /**
     * Replenish the budget of the given queue by the appropriate amount.
     * 
     * @param queue queue to replenish
     */
    private void replenishSessionBalance(WorkQueue queue) {
        UURI contextUri = queue.getContextUURI(this); 
        
        // TODO: consider confusing cross-effects of this and IP-based politeness
        queue.setSessionBalance(((Integer) getUncheckedAttribute(contextUri,
                ATTR_BALANCE_REPLENISH_AMOUNT)).intValue());
        // reset total budget (it may have changed)
        // TODO: is this the best way to be sensitive to potential mid-crawl changes
        long totalBudget = ((Long)getUncheckedAttribute(contextUri,ATTR_QUEUE_TOTAL_BUDGET)).longValue();
        queue.setTotalBudget(totalBudget);
    }

    /**
     * Enqueue the given queue to either readyClassQueues or inactiveQueues,
     * as appropriate.
     * 
     * @param wq
     */
    private void reenqueueQueue(WorkQueue wq) {
        if(wq.isOverBudget()) {
            // if still over budget, deactivate
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("DEACTIVATED queue: " +
                    wq.getClassKey());
            }
            deactivateQueue(wq);
        } else {
            readyQueue(wq);
        }
    }
    
    /**
     * Wake any queues sitting in the snoozed queue whose time has come.
     */
    void wakeQueues() {
        synchronized (snoozedClassQueues) {
            long now = System.currentTimeMillis();
            long nextWakeDelay = 0;
            int wokenQueuesCount = 0;
            while (true) {
                if (snoozedClassQueues.isEmpty()) {
                    return;
                }
                WorkQueue peek = (WorkQueue) snoozedClassQueues.first();
                nextWakeDelay = peek.getWakeTime() - now;
                if (nextWakeDelay <= 0) {
                    snoozedClassQueues.remove(peek);
                    peek.setWakeTime(0);
                    reenqueueQueue(peek);
                    wokenQueuesCount++;
                } else {
                    break;
                }
            }
            this.nextWake = new WakeTask();
            this.wakeTimer.schedule(nextWake,nextWakeDelay);
        }
    }

    /**
     * Note that the previously emitted CrawlURI has completed
     * its processing (for now).
     *
     * The CrawlURI may be scheduled to retry, if appropriate,
     * and other related URIs may become eligible for release
     * via the next next() call, as a result of finished().
     *
     *  (non-Javadoc)
     * @see org.archive.crawler.framework.Frontier#finished(org.archive.crawler.datamodel.CrawlURI)
     */
    public void finished(CrawlURI curi) {
        long now = System.currentTimeMillis();

        curi.incrementFetchAttempts();
        logLocalizedErrors(curi);
        WorkQueue wq = (WorkQueue) curi.getHolder();
        assert (wq.peek(this) == curi) : "unexpected peek " + wq;
        inProcessQueues.remove(wq, 1);

        if(includesRetireDirective(curi)) {
            // CrawlURI is marked to trigger retirement of its queue
            curi.processingCleanup();
            wq.unpeek();
            wq.update(this, curi); // rewrite any changes
            retireQueue(wq);
            return;
        }
        
        if (needsRetrying(curi)) {
            // Consider errors which can be retried, leaving uri atop queue
            if(curi.getFetchStatus()!=S_DEFERRED) {
                wq.expend(getCost(curi)); // all retries but DEFERRED cost
            }
            long delay_sec = retryDelayFor(curi);
            curi.processingCleanup(); // lose state that shouldn't burden retry
            synchronized(wq) {
                wq.unpeek();
                // TODO: consider if this should happen automatically inside unpeek()
                wq.update(this, curi); // rewrite any changes
                if (delay_sec > 0) {
                    long delay_ms = delay_sec * 1000;
                    snoozeQueue(wq, now, delay_ms);
                } else {
                    reenqueueQueue(wq);
                }
            }
            // Let everyone interested know that it will be retried.
            controller.fireCrawledURINeedRetryEvent(curi);
            doJournalRescheduled(curi);
            return;
        }

        // Curi will definitely be disposed of without retry, so remove from queue
        wq.dequeue(this);
        decrementQueuedCount(1);
        log(curi);

        if (curi.isSuccess()) {
            totalProcessedBytes += curi.getRecordedSize();
            incrementSucceededFetchCount();
            // Let everyone know in case they want to do something before we strip the curi.
            controller.fireCrawledURISuccessfulEvent(curi);
            doJournalFinishedSuccess(curi);
            wq.expend(getCost(curi)); // successes cost
        } else if (isDisregarded(curi)) {
            // Check for codes that mean that while we the crawler did
            // manage to schedule it, it must be disregarded for some reason.
            incrementDisregardedUriCount();
            // Let interested listeners know of disregard disposition.
            controller.fireCrawledURIDisregardEvent(curi);
            doJournalDisregarded(curi);
            // if exception, also send to crawlErrors
            if (curi.getFetchStatus() == S_RUNTIME_EXCEPTION) {
                Object[] array = { curi };
                controller.runtimeErrors.log(Level.WARNING, curi.getUURI()
                        .toString(), array);
            }
            // TODO: consider reinstating forget-uri
        } else {
            // In that case FAILURE, note & log
            //Let interested listeners know of failed disposition.
            this.controller.fireCrawledURIFailureEvent(curi);
            // if exception, also send to crawlErrors
            if (curi.getFetchStatus() == S_RUNTIME_EXCEPTION) {
                Object[] array = { curi };
                this.controller.runtimeErrors.log(Level.WARNING, curi.getUURI()
                        .toString(), array);
            }
            incrementFailedFetchCount();
            // let queue note error
            wq.noteError(((Integer) getUncheckedAttribute(curi,
                    ATTR_ERROR_PENALTY_AMOUNT)).intValue()); 
            doJournalFinishedFailure(curi);
            wq.expend(getCost(curi)); // failures cost
        }

        long delay_ms = politenessDelayFor(curi);
        synchronized(wq) {
            if (delay_ms > 0) {
                snoozeQueue(wq,now,delay_ms);
            } else {
                reenqueueQueue(wq);
            }
        }

        curi.stripToMinimal();
        curi.processingCleanup();

    }

    private boolean includesRetireDirective(CrawlURI curi) {
        return curi.containsKey(A_FORCE_RETIRE) && (Boolean)curi.getObject(A_FORCE_RETIRE);
    }

    /**
     * Place the given queue into 'snoozed' state, ineligible to
     * supply any URIs for crawling, for the given amount of time. 
     * 
     * @param wq queue to snooze 
     * @param now time now in ms 
     * @param delay_ms time to snooze in ms
     */
    private void snoozeQueue(WorkQueue wq, long now, long delay_ms) {
        long nextTime = now + delay_ms;
        wq.setWakeTime(nextTime);
        long snoozeToInactiveDelayMs = ((Long)getUncheckedAttribute(null,
                ATTR_SNOOZE_DEACTIVATE_MS)).longValue();
        if (delay_ms > snoozeToInactiveDelayMs && !inactiveQueues.isEmpty()) {
            deactivateQueue(wq);
        } else {
            synchronized(snoozedClassQueues) {
                snoozedClassQueues.add(wq);
                if(wq == snoozedClassQueues.first()) {
                    this.nextWake = new WakeTask();
                    this.wakeTimer.schedule(nextWake, delay_ms);
                }
            }
        }
    }

    /**
     * Forget the given CrawlURI. This allows a new instance
     * to be created in the future, if it is reencountered under
     * different circumstances.
     *
     * @param curi The CrawlURI to forget
     */
    protected void forget(CrawlURI curi) {
        logger.finer("Forgetting " + curi);
        alreadyIncluded.forget(canonicalize(curi.getUURI()), curi);
    }

    /**  (non-Javadoc)
     * @see org.archive.crawler.framework.Frontier#discoveredUriCount()
     */
    public long discoveredUriCount() {
        return (this.alreadyIncluded != null)? this.alreadyIncluded.count(): 0;
    }

    /**
     * Delete all scheduled URIs matching the given regex. 
     * 
     * @param match regex of URIs to delete
     * @return Number of items deleted.
     */
    public long deleteURIs(String uriMatch) {
        return deleteURIs(uriMatch,null);
    }

    /**
     * Delete all scheduled URIs matching the given regex, in queues with
     * names matching the second given regex. 
     * 
     * @param uriMatch regex of URIs to delete
     * @param queueMatch regex of queues to affect, or null for all
     * @return Number of items deleted.
     */
    public long deleteURIs(String uriMatch, String queueMatch) {
        long count = 0;
        // TODO: DANGER/ values() may not work right from CachedBdbMap
        Iterator<String> iter = allQueues.keySet().iterator(); 
        while(iter.hasNext()) {
            String queueKey = ((String)iter.next());
            if(StringUtils.isNotEmpty(queueMatch) && !queueKey.matches(queueMatch)) {
                // skip this queue
                continue; 
            }
            WorkQueue wq = getQueueFor(queueKey);
            wq.unpeek();
            count += wq.deleteMatching(this, uriMatch);
        }
        decrementQueuedCount(count);
        return count;
    }
    
    //
    // Reporter implementation
    //
    
    public static String STANDARD_REPORT = "standard";
    public static String ALL_NONEMPTY = "nonempty";
    public static String ALL_QUEUES = "all";
    protected static String[] REPORTS = {STANDARD_REPORT,ALL_NONEMPTY,ALL_QUEUES};
    
    public String[] getReports() {
        return REPORTS;
    }
    
    /**
     * @param w Where to write to.
     */
    public void singleLineReportTo(PrintWriter w) {
        if (this.allQueues == null) {
            return;
        }
        int allCount = allQueues.size();
        int inProcessCount = inProcessQueues.uniqueSet().size();
        int readyCount = readyClassQueues.size();
        int snoozedCount = snoozedClassQueues.size();
        int activeCount = inProcessCount + readyCount + snoozedCount;
        int inactiveCount = inactiveQueues.size();
        int retiredCount = retiredQueues.size();
        int exhaustedCount = 
            allCount - activeCount - inactiveCount - retiredCount;
        w.print(allCount);
        w.print(" queues: ");
        w.print(activeCount);
        w.print(" active (");
        w.print(inProcessCount);
        w.print(" in-process; ");
        w.print(readyCount);
        w.print(" ready; ");
        w.print(snoozedCount);
        w.print(" snoozed); ");
        w.print(inactiveCount);
        w.print(" inactive; ");
        w.print(retiredCount);
        w.print(" retired; ");
        w.print(exhaustedCount);
        w.print(" exhausted");
        w.flush();
    }
    
    /* (non-Javadoc)
     * @see org.archive.util.Reporter#singleLineLegend()
     */
    public String singleLineLegend() {
        return "total active in-process ready snoozed inactive retired exhausted";
    }

    /**
     * This method compiles a human readable report on the status of the frontier
     * at the time of the call.
     * @param name Name of report.
     * @param writer Where to write to.
     */
    public synchronized void reportTo(String name, PrintWriter writer) {
        if(ALL_NONEMPTY.equals(name)) {
            allNonemptyReportTo(writer);
            return;
        }
        if(ALL_QUEUES.equals(name)) {
            allQueuesReportTo(writer);
            return;
        }
        if(name!=null && !STANDARD_REPORT.equals(name)) {
            writer.print(name);
            writer.print(" unavailable; standard report:\n");
        }
        standardReportTo(writer);
    }   
    
    /** Compact report of all nonempty queues (one queue per line)
     * 
     * @param writer
     */
    private void allNonemptyReportTo(PrintWriter writer) {
        ArrayList<WorkQueue> inProcessQueuesCopy;
        synchronized(this.inProcessQueues) {
            // grab a copy that will be stable against mods for report duration 
            @SuppressWarnings("unchecked")
            Collection<WorkQueue> inProcess = this.inProcessQueues;
            inProcessQueuesCopy = new ArrayList<WorkQueue>(inProcess);
        }
        writer.print("\n -----===== IN-PROCESS QUEUES =====-----\n");
        queueSingleLinesTo(writer, inProcessQueuesCopy.iterator());

        writer.print("\n -----===== READY QUEUES =====-----\n");
        queueSingleLinesTo(writer, this.readyClassQueues.iterator());

        writer.print("\n -----===== SNOOZED QUEUES =====-----\n");
        queueSingleLinesTo(writer, this.snoozedClassQueues.iterator());
        
        writer.print("\n -----===== INACTIVE QUEUES =====-----\n");
        queueSingleLinesTo(writer, this.inactiveQueues.iterator());
        
        writer.print("\n -----===== RETIRED QUEUES =====-----\n");
        queueSingleLinesTo(writer, this.retiredQueues.iterator());
    }

    /** Compact report of all nonempty queues (one queue per line)
     * 
     * @param writer
     */
    private void allQueuesReportTo(PrintWriter writer) {
        queueSingleLinesTo(writer, allQueues.keySet().iterator());
    }
    
    /**
     * Write the single-line reports of all queues in the
     * iterator to the writer 
     * 
     * @param writer to receive report
     * @param iterator over queues of interest.
     */
    private void queueSingleLinesTo(PrintWriter writer, Iterator<?> iterator) {
        Object obj;
        WorkQueue q;
        boolean legendWritten = false;
        while( iterator.hasNext()) {
            obj = iterator.next();
            if (obj ==  null) {
                continue;
            }
            q = (obj instanceof WorkQueue)?
                (WorkQueue)obj:
                (WorkQueue)this.allQueues.get((String)obj);
            if(q == null) {
                writer.print(" ERROR: "+obj);
            }
            if(!legendWritten) {
                writer.println(q.singleLineLegend());
                legendWritten = true;
            }
            q.singleLineReportTo(writer);
        }       
    }

    /**
     * @param w Writer to print to.
     */
    private void standardReportTo(PrintWriter w) {
        int allCount = allQueues.size();
        int inProcessCount = inProcessQueues.uniqueSet().size();
        int readyCount = readyClassQueues.size();
        int snoozedCount = snoozedClassQueues.size();
        int activeCount = inProcessCount + readyCount + snoozedCount;
        int inactiveCount = inactiveQueues.size();
        int retiredCount = retiredQueues.size();
        int exhaustedCount = 
            allCount - activeCount - inactiveCount - retiredCount;

        w.print("Frontier report - ");
        w.print(ArchiveUtils.get12DigitDate());
        w.print("\n");
        w.print(" Job being crawled: ");
        w.print(controller.getOrder().getCrawlOrderName());
        w.print("\n");
        w.print("\n -----===== STATS =====-----\n");
        w.print(" Discovered:    ");
        w.print(Long.toString(discoveredUriCount()));
        w.print("\n");
        w.print(" Queued:        ");
        w.print(Long.toString(queuedUriCount()));
        w.print("\n");
        w.print(" Finished:      ");
        w.print(Long.toString(finishedUriCount()));
        w.print("\n");
        w.print("  Successfully: ");
        w.print(Long.toString(succeededFetchCount()));
        w.print("\n");
        w.print("  Failed:       ");
        w.print(Long.toString(failedFetchCount()));
        w.print("\n");
        w.print("  Disregarded:  ");
        w.print(Long.toString(disregardedUriCount()));
        w.print("\n");
        w.print("\n -----===== QUEUES =====-----\n");
        w.print(" Already included size:     ");
        w.print(Long.toString(alreadyIncluded.count()));
        w.print("\n");
        w.print("               pending:     ");
        w.print(Long.toString(alreadyIncluded.pending()));
        w.print("\n");
        w.print("\n All class queues map size: ");
        w.print(Long.toString(allCount));
        w.print("\n");
        w.print( "             Active queues: ");
        w.print(activeCount);
        w.print("\n");
        w.print("                    In-process: ");
        w.print(inProcessCount);
        w.print("\n");
        w.print("                         Ready: ");
        w.print(readyCount);
        w.print("\n");
        w.print("                       Snoozed: ");
        w.print(snoozedCount);
        w.print("\n");
        w.print("           Inactive queues: ");
        w.print(inactiveCount);
        w.print("\n");
        w.print("            Retired queues: ");
        w.print(retiredCount);
        w.print("\n");
        w.print("          Exhausted queues: ");
        w.print(exhaustedCount);
        w.print("\n");
        
        w.print("\n -----===== IN-PROCESS QUEUES =====-----\n");
        @SuppressWarnings("unchecked")
        Collection<WorkQueue> inProcess = inProcessQueues;
        ArrayList<WorkQueue> copy = extractSome(inProcess, REPORT_MAX_QUEUES);
        appendQueueReports(w, copy.iterator(), copy.size(), REPORT_MAX_QUEUES);
        
        w.print("\n -----===== READY QUEUES =====-----\n");
        appendQueueReports(w, this.readyClassQueues.iterator(),
            this.readyClassQueues.size(), REPORT_MAX_QUEUES);

        w.print("\n -----===== SNOOZED QUEUES =====-----\n");
        copy = extractSome(snoozedClassQueues, REPORT_MAX_QUEUES);
        appendQueueReports(w, copy.iterator(), copy.size(), REPORT_MAX_QUEUES);
        
        WorkQueue longest = longestActiveQueue;
        if (longest != null) {
            w.print("\n -----===== LONGEST QUEUE =====-----\n");
            longest.reportTo(w);
        }

        w.print("\n -----===== INACTIVE QUEUES =====-----\n");
        appendQueueReports(w, this.inactiveQueues.iterator(),
            this.inactiveQueues.size(), REPORT_MAX_QUEUES);
        
        w.print("\n -----===== RETIRED QUEUES =====-----\n");
        appendQueueReports(w, this.retiredQueues.iterator(),
            this.retiredQueues.size(), REPORT_MAX_QUEUES);

        w.flush();
    }
    
    
    /**
     * Extract some of the elements in the given collection to an
     * ArrayList.  This method synchronizes on the given collection's
     * monitor.  The returned list will never contain more than the
     * specified maximum number of elements.
     * 
     * @param c    the collection whose elements to extract
     * @param max  the maximum number of elements to extract
     * @return  the extraction
     */
    private static <T> ArrayList<T> extractSome(Collection<T> c, int max) {
        // Try to guess a sane initial capacity for ArrayList
        // Hopefully given collection won't grow more than 10 items
        // between now and the synchronized block...
        int initial = Math.min(c.size() + 10, max);
        int count = 0;
        ArrayList<T> list = new ArrayList<T>(initial);
        synchronized (c) {
            Iterator<T> iter = c.iterator();
            while (iter.hasNext() && (count < max)) {
                list.add(iter.next());
                count++;
            }
        }
        return list;
    }

    /**
     * Append queue report to general Frontier report.
     * @param w StringBuffer to append to.
     * @param iterator An iterator over 
     * @param total
     * @param max
     */
    protected void appendQueueReports(PrintWriter w, Iterator<?> iterator,
            int total, int max) {
        Object obj;
        WorkQueue q;
        for(int count = 0; iterator.hasNext() && (count < max); count++) {
            obj = iterator.next();
            if (obj ==  null) {
                continue;
            }
            q = (obj instanceof WorkQueue)?
                (WorkQueue)obj:
                (WorkQueue)this.allQueues.get((String)obj);
            if(q == null) {
                w.print("WARNING: No report for queue "+obj);
            }
            q.reportTo(w);
        }
        if(total > max) {
            w.print("...and " + (total - max) + " more.\n");
        }
    }

    /**
     * Force logging, etc. of operator- deleted CrawlURIs
     * 
     * @see org.archive.crawler.framework.Frontier#deleted(org.archive.crawler.datamodel.CrawlURI)
     */
    public synchronized void deleted(CrawlURI curi) {
        //treat as disregarded
        controller.fireCrawledURIDisregardEvent(curi);
        log(curi);
        incrementDisregardedUriCount();
        curi.stripToMinimal();
        curi.processingCleanup();
    }

    public void considerIncluded(UURI u) {
        this.alreadyIncluded.note(canonicalize(u));
        CrawlURI temp = new CrawlURI(u);
        temp.setClassKey(getClassKey(temp));
        getQueueFor(temp).expend(getCost(temp));
    }
    
    protected abstract void initQueue() throws IOException;
    protected abstract void closeQueue() throws IOException;
    
    /**
     * Returns <code>true</code> if the WorkQueue implementation of this
     * Frontier stores its workload on disk instead of relying
     * on serialization mechanisms.
     * 
     * TODO: rename! (this is a very misleading name) or kill (don't
     * see any implementations that return false)
     * 
     * @return a constant boolean value for this class/instance
     */
    protected abstract boolean workQueueDataOnDisk();
    
    
    public FrontierGroup getGroup(CrawlURI curi) {
        return getQueueFor(curi);
    }
    
    
    public long averageDepth() {
        int inProcessCount = inProcessQueues.uniqueSet().size();
        int readyCount = readyClassQueues.size();
        int snoozedCount = snoozedClassQueues.size();
        int activeCount = inProcessCount + readyCount + snoozedCount;
        int inactiveCount = inactiveQueues.size();
        int totalQueueCount = (activeCount+inactiveCount);
        return (totalQueueCount == 0) ? 0 : liveQueuedUriCount.get() / totalQueueCount;
    }
    public float congestionRatio() {
        int inProcessCount = inProcessQueues.uniqueSet().size();
        int readyCount = readyClassQueues.size();
        int snoozedCount = snoozedClassQueues.size();
        int activeCount = inProcessCount + readyCount + snoozedCount;
        int inactiveCount = inactiveQueues.size();
        return (float)(activeCount + inactiveCount) / (inProcessCount + snoozedCount);
    }
    public long deepestUri() {
        return longestActiveQueue==null ? -1 : longestActiveQueue.getCount();
    }
    
    
    /* (non-Javadoc)
     * @see org.archive.crawler.framework.Frontier#isEmpty()
     */
    public synchronized boolean isEmpty() {
        return liveQueuedUriCount.get() == 0 && alreadyIncluded.pending() == 0;
    }
}

