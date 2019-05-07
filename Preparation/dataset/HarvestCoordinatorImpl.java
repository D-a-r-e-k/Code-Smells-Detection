/*
 *  Copyright 2006 The National Library of New Zealand
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.webcurator.core.harvester.coordinator;

import java.io.File;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webcurator.core.archive.SipBuilder;
import org.webcurator.core.common.Environment;
import org.webcurator.core.common.EnvironmentFactory;
import org.webcurator.core.exceptions.DigitalAssetStoreException;
import org.webcurator.core.exceptions.WCTRuntimeException;
import org.webcurator.core.harvester.agent.HarvestAgent;
import org.webcurator.core.harvester.agent.HarvestAgentFactory;
import org.webcurator.core.notification.InTrayManager;
import org.webcurator.core.notification.MessageType;
import org.webcurator.core.profiles.HeritrixProfile;
import org.webcurator.core.reader.LogReader;
import org.webcurator.core.scheduler.TargetInstanceManager;
import org.webcurator.core.store.DigitalAssetStoreFactory;
import org.webcurator.core.store.DigitalAssetStore;
import org.webcurator.core.harvester.agent.HarvestAgentSOAPClient;
import org.webcurator.core.harvester.agent.HarvestAgentConfig;
import org.webcurator.core.targets.TargetManager;
import org.webcurator.core.util.Auditor;
import org.webcurator.domain.HarvestCoordinatorDAO;
import org.webcurator.domain.TargetInstanceCriteria;
import org.webcurator.domain.TargetInstanceDAO;
import org.webcurator.domain.model.auth.Privilege;
import org.webcurator.domain.model.core.ArcHarvestFile;
import org.webcurator.domain.model.core.ArcHarvestFileDTO;
import org.webcurator.domain.model.core.ArcHarvestResource;
import org.webcurator.domain.model.core.ArcHarvestResourceDTO;
import org.webcurator.domain.model.core.ArcHarvestResult;
import org.webcurator.domain.model.core.ArcHarvestResultDTO;
import org.webcurator.domain.model.core.BandwidthRestriction;
import org.webcurator.domain.model.core.BusinessObjectFactory;
import org.webcurator.domain.model.core.HarvestResourceDTO;
import org.webcurator.domain.model.core.HarvestResult;
import org.webcurator.domain.model.core.HarvestResultDTO;
import org.webcurator.domain.model.core.HarvesterStatus;
import org.webcurator.domain.model.core.LogFilePropertiesDTO;
import org.webcurator.domain.model.core.Schedule;
import org.webcurator.domain.model.core.Seed;
import org.webcurator.domain.model.core.SeedHistory;
import org.webcurator.domain.model.core.Target;
import org.webcurator.domain.model.core.TargetInstance;
import org.webcurator.domain.model.core.harvester.agent.HarvestAgentStatusDTO;
import org.webcurator.domain.model.core.harvester.agent.HarvesterStatusDTO;
import org.webcurator.domain.model.dto.QueuedTargetInstanceDTO;

/**
 * 
 * @author nwaight
 */
public class HarvestCoordinatorImpl implements HarvestCoordinator {
    
	/** The list of harvest agent status records. */
    private HashMap<String, HarvestAgentStatusDTO> harvestAgents;
    
    /** the object for persisting harvest coordinator data, */
    private HarvestCoordinatorDAO harvestCoordinatorDao; 
    
    private TargetInstanceManager targetInstanceManager; 
    
    /** the object for accessing target instance data. */
    private TargetInstanceDAO targetInstanceDao;
    
    /** The factory for getting a harvest agent. */
    private HarvestAgentFactory harvestAgentFactory;  
    
    private DigitalAssetStoreFactory digitalAssetStoreFactory;    
    
    private HarvestAgentConfig harvestAgentConfig;    
    
    /** The Target Manager. */
    private TargetManager targetManager;
    
    /** The InTrayManager. */
    private InTrayManager inTrayManager;
    
    /** The minimum bandwidth setting. */
    private int minimumBandwidth = 1;
    
    /** The maximum bandwidth percentage allocated to specific jobs. */
    private int maxBandwidthPercent = 80;
    
    /** the number of days before a target instance's digital assets are purged from the DAS. */
    private int daysBeforeDASPurge = 14;
    
    /** the number of days before an aborted target instance's remnant data are purged from the system. */
    private int daysBeforeAbortedTargetInstancePurge = 7;
    
    /** The max global bandwidth setting from the previous check. */
    private long previousMaxGlobalBandwidth = 0;
    
    /** The Auditor to use */
    private Auditor auditor = null;
    
    private SipBuilder sipBuilder = null;
    
    /** The logger. */
    private Log log;
    
    /** Queue paused flag. */
    private boolean queuePaused = false;
    
    /** Automatic QA Url */
    private String autoQAUrl = "";
    
  
    /** Default Constructor. */
    public HarvestCoordinatorImpl() {
        super();
        log = LogFactory.getLog(getClass());        
        harvestAgents = new HashMap<String, HarvestAgentStatusDTO>();
    }

    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentListener#heartbeat(org.webcurator.core.harvester.agent.HarvestAgentStatus)
     */
    public void heartbeat(HarvestAgentStatusDTO aStatus) {    
        if (log.isInfoEnabled()) {
            if (harvestAgents.containsKey(aStatus.getName())) {
                if (log.isDebugEnabled()) {
                    log.debug("Updating status for " + aStatus.getName());
                }
            }
            else {
                log.info("Registering harvest agent " + aStatus.getName());
            }
        }
        
        aStatus.setLastUpdated(new Date());
        harvestAgents.put(aStatus.getName(), aStatus);
                
        String key = "";
        long toid = 0;
        TargetInstance ti = null;
        HarvesterStatusDTO sd = null;
        HashMap hss = null;
        HarvesterStatus status = null;
        Iterator it = aStatus.getHarvesterStatus().keySet().iterator();
        while (it.hasNext()) {
            key = (String) it.next();            
            toid = Long.parseLong(key.substring(key.lastIndexOf("-") + 1));
            ti = targetInstanceDao.load(toid);
            hss = aStatus.getHarvesterStatus();            
            sd = (HarvesterStatusDTO) hss.get(key);
            
            //Update the harvesterStatus with current versions
            Environment env = EnvironmentFactory.getEnv();
            sd.setApplicationVersion(env.getApplicationVersion());
            sd.setHeritrixVersion(env.getHeritrixVersion());
            
            if (ti.getStatus() == null) {
                status = new HarvesterStatus(sd);
                ti.setStatus(status);
                status.setOid(ti.getOid());
            }
            else {
                status = ti.getStatus();
                status.update(sd);
            }
            
            if (status.getStatus().startsWith("Paused")) {
            	if (ti.getState().equals(TargetInstance.STATE_RUNNING)) {
            		ti.setState(TargetInstance.STATE_PAUSED);
            	}
            }
            
            //We have seen cases where a running Harvest is showing as Queued 
            //in the UI. Once in this state, the user has no control over the 
            //harvest and cannot use it. This work around means that any 
            //TIs in the wrong state will be corrected on the next heartbeat
            if (status.getStatus().startsWith("Running")) {
            	if (ti.getState().equals(TargetInstance.STATE_PAUSED) ||
            		ti.getState().equals(TargetInstance.STATE_QUEUED)) {
            		
            		if(ti.getState().equals(TargetInstance.STATE_QUEUED))
            		{
               	        log.info("HarvestCoordinator: Target Instance state changed from Queued to Running for target instance "+ti.getOid().toString());
            		}
            		if(ti.getActualStartTime() == null)
            		{
            			//This was not set up correctly when harvest was initiated
            			Date now = new Date();
            			Date startTime = new Date(now.getTime() - status.getElapsedTime());
            			ti.setActualStartTime(startTime);
            			ti.setHarvestServer(aStatus.getName());
            			
            	        log.info("HarvestCoordinator: Target Instance start time set for target instance "+ti.getOid().toString());
            		}
            		ti.setState(TargetInstance.STATE_RUNNING);
            	}
            }
            
            if (status.getStatus().startsWith("Finished")) {
            	if (ti.getState().equals(TargetInstance.STATE_RUNNING)) {
            		ti.setState(TargetInstance.STATE_STOPPING);
            	}
            }
            
            // This is a required because when a "Could not launch job - Fatal InitializationException" job occurs
            // We do not get a notification that causes the job to stop nicely
            if (status.getStatus().startsWith("Could not launch job - Fatal InitializationException")) {
            	if (ti.getState().equals(TargetInstance.STATE_RUNNING)) {
            		ti.setState(TargetInstance.STATE_ABORTED);
            		HarvestAgentStatusDTO hs = getHarvestAgent(ti.getJobName());        
                    if (hs == null) {
                        if (log.isWarnEnabled()) {
                            log.warn("Forced Abort Failed. Failed to find the Harvest Agent for the Job " + ti.getJobName() + ".");
                        }
                    }
                    else {
                    	HarvestAgent agent = harvestAgentFactory.getHarvestAgent(hs.getHost(), hs.getPort());                            
                        agent.abort(ti.getJobName());
                    }                      
            	}
            }
                         
            targetInstanceManager.save(ti);
        }    
    }

    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentListener#harvestComplete(org.webcurator.core.model.HarvestResult)
     */
    public void harvestComplete(HarvestResultDTO aResult) {
        TargetInstance ti = targetInstanceDao.load(aResult.getTargetInstanceOid());
        if (ti == null) {
            throw new WCTRuntimeException("Unknown TargetInstance oid recieved " + aResult.getTargetInstanceOid() + " failed to save HarvestResult.");
        }
        
        //The result is for the original harvest, but the TI already has one or more results
        if(aResult.getHarvestNumber() == 1 && !ti.getHarvestResults().isEmpty())
      	{
        	//This is a repeat message probably due to a timeout. Leaving this to run
        	//would generate a second 'Original Harvest' which will subsequently fail in indexing 
        	//due to a duplicate file name constraint in the arc_harvest_file table
        	log.warn("Duplicate 'Harvest Complete' message received for job: "+ti.getOid()+". Message ignored.");
      		return;
      	}
        		
    	log.info("'Harvest Complete' message received for job: "+ti.getOid()+".");
    	
        HarvestResult harvestResult = null;
        if (aResult instanceof ArcHarvestResultDTO) {
            harvestResult = new ArcHarvestResult((ArcHarvestResultDTO) aResult, ti); 
        }        
        else {
            harvestResult = new HarvestResult(aResult, ti);
        }
        
        harvestResult.setState(HarvestResult.STATE_INDEXING);
                
        List<HarvestResult> hrs = ti.getHarvestResults();
        hrs.add(harvestResult);
        ti.setHarvestResults(hrs);        

        ti.setState(TargetInstance.STATE_HARVESTED);
        
        targetInstanceDao.save(harvestResult);
        targetInstanceDao.save(ti);
        sendBandWidthRestrictions();
        
        // IF the associated target record for this TI has
        // no active TIs remaining (scheduled, queued, running, 
        // paused, stopping)
        // AND
        // the target's schedule is not active (i.e we're past
        // the schedule end date),
        // THEN set the status of the associated target to 'complete'.
        //
		boolean bActiveSchedules = false;
		for(Schedule schedule: ti.getTarget().getSchedules()) {
			if(schedule.getEndDate() == null) {
				bActiveSchedules = true;
			}
			else if (schedule.getEndDate().after(new Date())){
				bActiveSchedules = true;
			}
		}		

        if (targetInstanceDao.countActiveTIsForTarget(ti.getTarget().getOid()) == 0 && !bActiveSchedules) {
        	Target t = targetManager.load(ti.getTarget().getOid(), true);
        	t.changeState(Target.STATE_COMPLETED);
        	targetManager.save(t);
        }
        
        // Ask the DigitalAssetStore to index the ARC
        try {
        	digitalAssetStoreFactory.getDAS().initiateIndexing(new ArcHarvestResultDTO( harvestResult.getOid(), harvestResult.getTargetInstance().getOid(), harvestResult.getCreationDate(), harvestResult.getHarvestNumber(), harvestResult.getProvenanceNote() ));
        }
        catch(DigitalAssetStoreException ex) { 
        	log.error("Could not send initiateIndexing message to the DAS", ex);
        }
        
        inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_INSTANCE_COMPLETE, ti);
        inTrayManager.generateTask(Privilege.ENDORSE_HARVEST,MessageType.TARGET_INSTANCE_ENDORSE,ti);
        
    	log.info("'Harvest Complete' message processed for job: "+ti.getOid()+".");
    }
    
    private void cleanHarvestResult(HarvestResult harvestResult)
    {
    	if(harvestResult != null)
    	{
	    	if(harvestResult.getResources() != null)
	    	{
		    	targetInstanceDao.deleteHarvestResultResources(harvestResult.getOid());
	    	}
	    	
	    	if(harvestResult instanceof ArcHarvestResult)
	    	{
		    	targetInstanceDao.deleteHarvestResultFiles(harvestResult.getOid());
	    	}
    	}
    }
    
    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#reIndexHarvestResult(HarvestResult)
     */
	public Boolean reIndexHarvestResult(HarvestResult origHarvestResult) {
        TargetInstance ti = origHarvestResult.getTargetInstance();
        
		//Assume we are already indexing
		Boolean reIndex = false;
		
		try
		{
			reIndex = !digitalAssetStoreFactory.getDAS().checkIndexing(origHarvestResult.getOid());
		}
        catch(DigitalAssetStoreException ex) { 
        	log.error("Could not send checkIndexing message to the DAS", ex);
        }
        
		if(reIndex)
		{
			//Save any unsaved changes
	        targetInstanceDao.save(ti);

	        //remove any HarvestResources and ArcHarvestFiles
			cleanHarvestResult(origHarvestResult);
			
			//reload the targetInstance
			ti = targetInstanceDao.load(ti.getOid());
			
            HarvestResult newHarvestResult = null;
            if (origHarvestResult instanceof ArcHarvestResult) {
            	ArcHarvestResultDTO ahr = new ArcHarvestResultDTO();
	            ahr.setCreationDate(new Date());    
	            ahr.setTargetInstanceOid(ti.getOid());
	            ahr.setProvenanceNote(origHarvestResult.getProvenanceNote()); 
	            ahr.setHarvestNumber(origHarvestResult.getHarvestNumber()); 
	            newHarvestResult = new ArcHarvestResult(ahr, ti); 
            }        
            else {
            	HarvestResultDTO hr = new HarvestResultDTO();
	            hr.setCreationDate(new Date());    
	            hr.setTargetInstanceOid(ti.getOid());
	            hr.setProvenanceNote(origHarvestResult.getProvenanceNote()); 
	            hr.setHarvestNumber(origHarvestResult.getHarvestNumber()); 
            	newHarvestResult = new HarvestResult(hr, ti);
            }

            origHarvestResult.setState(HarvestResult.STATE_ABORTED);
            newHarvestResult.setState(HarvestResult.STATE_INDEXING);
	                
	        List<HarvestResult> hrs = ti.getHarvestResults();
	        hrs.add(newHarvestResult);
	        ti.setHarvestResults(hrs);        
	
	        ti.setState(TargetInstance.STATE_HARVESTED);
	        
	        targetInstanceDao.save(newHarvestResult);
	        targetInstanceDao.save(ti);
	        
	        try {
	        	digitalAssetStoreFactory.getDAS().initiateIndexing(new ArcHarvestResultDTO( newHarvestResult.getOid(), newHarvestResult.getTargetInstance().getOid(), newHarvestResult.getCreationDate(), newHarvestResult.getHarvestNumber(), newHarvestResult.getProvenanceNote() ));
		        
		        inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_INSTANCE_COMPLETE, ti);
		        inTrayManager.generateTask(Privilege.ENDORSE_HARVEST,MessageType.TARGET_INSTANCE_ENDORSE,ti);        
		    }
		    catch(DigitalAssetStoreException ex) { 
		      	log.error("Could not send initiateIndexing message to the DAS", ex);
		    }
		}
		
		return reIndex;
	}
    
    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentListener#notification(Long, String, String)
     */
    public void notification(Long aTargetInstanceOid, int notificationCategory, String aMessageType) {
        TargetInstance ti = targetInstanceDao.load(aTargetInstanceOid);
        inTrayManager.generateNotification(ti.getOwner().getOid(), notificationCategory, aMessageType, ti);  
    }
    
    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentListener#notification(String, String)
     */
    public void notification(String aSubject, int notificationCategory, String aMessage) {
        List<String> privs = new ArrayList<String>();
        privs.add(Privilege.MANAGE_WEB_HARVESTER);
        inTrayManager.generateNotification(privs, notificationCategory, aSubject,aMessage);     
    }
    
    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentCoordinator#harvest(TargetInstance)
     */
    public void harvest(TargetInstance aTargetInstance, HarvestAgentStatusDTO aHarvestAgent) {
        if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
        }
        
        if (aHarvestAgent == null) {
            throw new WCTRuntimeException("A null harvest agent status was provided to the harvest command.");
        }
                
        // if the target is not approved to be harvested then do not harvest
        if (queuePaused || !isTargetApproved(aTargetInstance) || aHarvestAgent.getMemoryWarning()) { 
        	return;
        }
        
        // Prepare the instance for harvesting by storing its current information.
        prepareHarvest(aTargetInstance);
        
        // Run the actual harvest.
        _harvest(aTargetInstance, aHarvestAgent);
    } 
    
    
    private void prepareHarvest(TargetInstance aTargetInstance) {
    	BusinessObjectFactory factory = new BusinessObjectFactory();
        Set<String> originalSeeds = new HashSet<String>();
        Set<SeedHistory> seedHistory = new HashSet<SeedHistory>();
        for(Seed seed : targetManager.getSeeds(aTargetInstance)) {
            originalSeeds.add(seed.getSeed());
            
            if(targetInstanceManager.getStoreSeedHistory())
            {
	            SeedHistory history = factory.newSeedHistory(aTargetInstance, seed); 
	            seedHistory.add(history);
            }
        }
        
        //Note that seed history should eventually supplant original seeds. Original seeds
        //has been implemented as a Hibernate collection of String and thus the 
        //target_instance_orig_seeds table has no id column, preventing further expansion
        //of the collection. The seed history needs to include the primary column. The
        //originalSeeds are used by the quality review (prune) tool to generate the to level
        //tree view. The original seeds has been left in for this purpose to support legacy 
        //target instances created prior to this release. Future releases may see the removal 
        //of this functionality in favour of the SeedHistory; meanwhile this can be turned off 
        //via the targetInstanceManager bean in wct_core.xml
        aTargetInstance.setOriginalSeeds(originalSeeds);
        if(targetInstanceManager.getStoreSeedHistory())
        {
        	aTargetInstance.setSeedHistory(seedHistory);
        }
        // Generate some of the SIP.
        Map<String,String> sipParts = sipBuilder.buildSipSections(aTargetInstance);
        aTargetInstance.setSipParts(sipParts);
        
        // Save the sip parts and seeds to the database.
        targetInstanceDao.save(aTargetInstance);    	
    }
    

    /**
     * Internal harvest method.
     * @param aTargetInstance The instance to harvest.
     * @param aHarvestAgent The agent to harvest on.
     */
    private void _harvest(TargetInstance aTargetInstance, HarvestAgentStatusDTO aHarvestAgent) {
        if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
        }
        
        if (aHarvestAgent == null) {
            throw new WCTRuntimeException("A null harvest agent status was provided to the harvest command.");
        }
                
        // if the target is not approved to be harvested then do not harvest
        if (!isTargetApproved(aTargetInstance) || aHarvestAgent.getMemoryWarning()) { 
        	return;
        }
        
        HarvestAgent agent = harvestAgentFactory.getHarvestAgent(aHarvestAgent.getHost(), aHarvestAgent.getPort());
                
        // Create the seeds file contents.
        StringBuffer seeds = new StringBuffer();
        Set<String> originalSeeds = aTargetInstance.getOriginalSeeds();        
        for(String seed: originalSeeds) { 
            seeds.append(seed);
            seeds.append("\n");
        }
        
        // Get the profile.
        String profile = getHarvestProfileString(aTargetInstance);
        
        // Initiate harvest on the remote harvest agent
        agent.initiateHarvest(aTargetInstance.getJobName(), profile, seeds.toString());
        
        // Update the state of the allocated Target Instance
        aTargetInstance.setActualStartTime(new Date());
        aTargetInstance.setState(TargetInstance.STATE_RUNNING);
        aTargetInstance.setHarvestServer(aHarvestAgent.getName());
        
        // Save the updated information.
        targetInstanceManager.save(aTargetInstance);
        
        log.info("HarvestCoordinator: Harvest initiated successfully for target instance "+aTargetInstance.getOid().toString());
               
        // Run the bandwidth calculations.
        sendBandWidthRestrictions();
    }     
    
        
    /** @see HarvestCoordinator#checkForBandwidthTransition(). */
    public synchronized void checkForBandwidthTransition() {
    	long currBW = getCurrentGlobalMaxBandwidth();
    	if (log.isDebugEnabled()) {
    		log.debug("Checking bandwidth. prev = " + previousMaxGlobalBandwidth + " curr = " + currBW);
    	}
    	
    	if (currBW != previousMaxGlobalBandwidth) {
    		if (log.isInfoEnabled()) {
    			log.info("Found bandwidth transition from " + previousMaxGlobalBandwidth + " to " + currBW + " re-calulating bandwidth settings.");
    		}
    		sendBandWidthRestrictions();
    	}
    	
    	previousMaxGlobalBandwidth = currBW;
    }    

    /**
	 * Get the profile string with the overrides applied.
	 * @return
	 */
	private String getHarvestProfileString(TargetInstance aTargetInstance) {
		
		String profileString = aTargetInstance.getTarget().getProfile().getProfile();
		
		//replace any ${TI_OID} tokens
		profileString = profileString.replace("${TI_OID}", aTargetInstance.getOid().toString());

		HeritrixProfile heritrixProfile = HeritrixProfile.fromString(profileString);
		
		if (aTargetInstance.getProfileOverrides().hasOverrides()) {
			log.info("Applying Profile Overrides for "+aTargetInstance.getOid());
			aTargetInstance.getProfileOverrides().apply(heritrixProfile);
		}
		
		heritrixProfile.setToeThreads(targetManager.getSeeds(aTargetInstance).size() * 2);
		return heritrixProfile.toString();
	}        
    
    /**
     * Send the bandwidth restrictions to all the harvest jobs in the 
     * running or paused states.   
     */
    private void sendBandWidthRestrictions() {
    	HarvestAgent agent = null;
    	HarvestAgentStatusDTO ha = null;
        TargetInstance ti = null;
                
        // Allocate the bandwidth
        HashMap<Long, TargetInstance> running = calculateBandwidthAllocation();
                
        Iterator it = running.values().iterator();
        while (it.hasNext()) {
            ti = (TargetInstance) it.next();   
            ha = getHarvestAgent(ti.getJobName());
            if (ha != null) {
            	agent = harvestAgentFactory.getHarvestAgent(ha.getHost(), ha.getPort());              	
            	if (ti.getAllocatedBandwidth() == null || ti.getAllocatedBandwidth().intValue() <= 0) {
            		// if we get to this point and the bandwidth is set to zero then set it to be one
            		// zero allows the harvester to use as much bandwidth as it likes.
            		ti.setAllocatedBandwidth(new Long(1));
            	}
				agent.restrictBandwidth(ti.getJobName(), ti.getAllocatedBandwidth().intValue());
            	targetInstanceDao.save(ti);
            }
        }  
    }    
    
    /** @see HarvestCoordinator#calculateBandwidthAllocation(TargetInstance). */
    private HashMap<Long, TargetInstance> calculateBandwidthAllocation(TargetInstance aTargetInstance) {
        //  Check to see if there are other running target instances with a percentage allocation.
        TargetInstanceCriteria tic = new TargetInstanceCriteria();   
        Set<String> states = new HashSet<String>();
		states.add(TargetInstance.STATE_RUNNING);
		states.add(TargetInstance.STATE_PAUSED);
        tic.setStates(states); 
        
        List<TargetInstance> runningTIs = targetInstanceDao.findTargetInstances(tic);   
        runningTIs.add(aTargetInstance);
        
        return calculateBandwidthAllocation(runningTIs);
    }
    
    /** @see HarvestCoordinator#calculateBandwidthAllocation(TargetInstance). */
    private HashMap<Long, TargetInstance> calculateBandwidthAllocation() {
        //  Check to see if there are other running target instances with a percentage allocation.
        TargetInstanceCriteria tic = new TargetInstanceCriteria();
        Set<String> states = new HashSet<String>();
		states.add(TargetInstance.STATE_RUNNING);
		states.add(TargetInstance.STATE_PAUSED);
        tic.setStates(states); 
        
        List<TargetInstance> runningTIs = targetInstanceDao.findTargetInstances(tic);   
        
        return calculateBandwidthAllocation(runningTIs);
    }
    
    /** @see HarvestCoordinator#calculateBandwidthAllocation(TargetInstance). */
    private HashMap<Long, TargetInstance> calculateBandwidthAllocation(List<TargetInstance> aRunningTargetInstances) {
        HashMap<Long, TargetInstance> results = new HashMap<Long, TargetInstance>();
        
        // Get the global max bandwidth setting for the current period.
        long maxBandwidth = getCurrentGlobalMaxBandwidth();        
        
        BandwidthCalculator.calculateBandwidthAllocation(aRunningTargetInstances, maxBandwidth, maxBandwidthPercent);
        
        return results;
    }
    
    /**
     * @return the current global maximum bandwidth.
     */
    public long getCurrentGlobalMaxBandwidth() {
        return getGlobalMaxBandwidth(0);
    }
    
    private long getGlobalMaxBandwidth(int aMillisBeforeNow) {
    	try {
	    	Calendar now = Calendar.getInstance(); 
	    	now.add(Calendar.MILLISECOND, (aMillisBeforeNow * -1)); 
	    	String time = BandwidthRestriction.TIMEONLY_FORMAT.format(now.getTime());
	    	Date date = BandwidthRestriction.FULLDATE_FORMAT.parse(BandwidthRestriction.DEFAULT_DATE + time); 
	    	String day = BandwidthRestriction.FULLDAY_FORMAT.format(now.getTime()).toUpperCase();
	
	    	BandwidthRestriction br = harvestCoordinatorDao.getBandwidthRestriction(day, date);
	    	if (br != null) { 
	    		return br.getBandwidth();
	    	}
    	}
    	catch (ParseException e) {
	    	if (log.isErrorEnabled()) {
	    		log.error("Failed to parse the date for the bandwith restriction : " + e.getMessage(), e);
	    	}
    	} 

    	return 0;
    	}

    
    /**
     * @return map of harvest agent status's
     */
    public HashMap<String, HarvestAgentStatusDTO> getHarvestAgents() {
        return harvestAgents;
    }
    
    /**
     * @return a harvest agent status for the specified job name
     */
    protected HarvestAgentStatusDTO getHarvestAgent(String aJobName) {
        if (harvestAgents == null || harvestAgents.isEmpty()) {
            return null;
        }

        HarvestAgentStatusDTO agent = null;
        Iterator it2 = null;
        HarvesterStatusDTO hs = null;
        Iterator it = harvestAgents.values().iterator();
        while (it.hasNext()) {
            agent = (HarvestAgentStatusDTO) it.next();
            if (agent.getHarvesterStatus() != null && !agent.getHarvesterStatus().isEmpty()) {
                it2 = agent.getHarvesterStatus().values().iterator();
                while (it2.hasNext()) {
                    hs = (HarvesterStatusDTO) it2.next();
                    if (hs.getJobName().equals(aJobName)) {
                        return agent;
                    }
                }
            }
        }

        return null;
    }

    /**
     * @param aHarvestCoordinatorDao The harvestCoordinatorDAO to set.
     */
    public void setHarvestCoordinatorDao(HarvestCoordinatorDAO aHarvestCoordinatorDao) {
        this.harvestCoordinatorDao = aHarvestCoordinatorDao;
    }
    
    /** @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#getBandwidthRestrictions(). */
    public HashMap<String, List<BandwidthRestriction>> getBandwidthRestrictions() {
        return harvestCoordinatorDao.getBandwidthRestrictions();
    }

    /** @see org.webcurator.domain.HarvestCoordinatorDAO#getBandwidthRestriction(Long). */
    public BandwidthRestriction getBandwidthRestriction(Long aOid) {        
        return harvestCoordinatorDao.getBandwidthRestriction(aOid);
    }
    
    /** @see org.webcurator.domain.HarvestCoordinatorDAO#getBandwidthRestriction(String, Date). */
    public BandwidthRestriction getBandwidthRestriction(String aDay, Date aTime) {
        return harvestCoordinatorDao.getBandwidthRestriction(aDay, aTime);
    }

    /** @see org.webcurator.domain.HarvestCoordinatorDAO#saveOrUpdate(BandwidthRestriction). */
    public void saveOrUpdate(BandwidthRestriction aBandwidthRestriction) {
    	boolean isNew = aBandwidthRestriction.getOid() == null;
        harvestCoordinatorDao.saveOrUpdate(aBandwidthRestriction);
        
        if(isNew) {
    		auditor.audit(BandwidthRestriction.class.getName(), aBandwidthRestriction.getOid(), Auditor.ACTION_NEW_BANDWIDTH_RESTRICTION, "New bandwidth restriction: " + aBandwidthRestriction.toString());
    	}
    	else {
    		auditor.audit(BandwidthRestriction.class.getName(), aBandwidthRestriction.getOid(), Auditor.ACTION_CHANGE_BANDWIDTH_RESTRICTION, "Bandwidth setting changed to: " + aBandwidthRestriction.toString());
    	}
    }

    /** @see org.webcurator.domain.HarvestCoordinatorDAO#delete(Object). */
    public void delete(BandwidthRestriction aBandwidthRestriction) {
    	auditor.audit(BandwidthRestriction.class.getName(), null, Auditor.ACTION_DELETE_BANDWIDTH_RESTRICTION, "Deleted bandwidth restriction: " + aBandwidthRestriction.toString());
        harvestCoordinatorDao.delete(aBandwidthRestriction);        
    }    

    /**
     * @param aMinimumBandwidth The minimumBandwidth to set.
     */
    public void setMinimumBandwidth(int aMinimumBandwidth) {
    	if (aMinimumBandwidth <= 0) {
    		aMinimumBandwidth = 1;
    	}
    	
        this.minimumBandwidth = aMinimumBandwidth;
    }

    /**
     * @param targetInstanceDao The targetInstanceDao to set.
     */
    public void setTargetInstanceDao(TargetInstanceDAO targetInstanceDao) {
        this.targetInstanceDao = targetInstanceDao;
    }

    /**
     * @return Returns the maxBandwidthPercent.
     */
    public int getMaxBandwidthPercent() {
        return maxBandwidthPercent;
    }

    /**
     * @param maxBandwidthPercent The maxBandwidthPercent to set.
     */
    public void setMaxBandwidthPercent(int maxBandwidthPercent) {
        this.maxBandwidthPercent = maxBandwidthPercent;
    }

    /**
     * @param harvestAgentFactory The harvestAgentFactory to set.
     */
    public void setHarvestAgentFactory(HarvestAgentFactory harvestAgentFactory) {
        this.harvestAgentFactory = harvestAgentFactory;
    }

    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#updateProfileOverrides(TargetInstance)
     */
    public void updateProfileOverrides(TargetInstance aTargetInstance) {
    	if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided.");
        }

        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("Update Profile Overrides Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
            }           
            return;
        }
        
        String profile = getHarvestProfileString(aTargetInstance);
        
        HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());                       
        agent.updateProfileOverrides(aTargetInstance.getJobName(), profile);  		
	}
    
    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#pause(TargetInstance)
     */
    public void pause(TargetInstance aTargetInstance) {
        if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
        }

        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("PAUSE Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
            }           
            return;
        }
        
        HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());
        
        // Update the state of the allocated Target Instance
        aTargetInstance.setState(TargetInstance.STATE_PAUSED);       
        targetInstanceDao.save(aTargetInstance);
        
        agent.pause(aTargetInstance.getJobName());        
    }

    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#resume(TargetInstance)
     */
    public void resume(TargetInstance aTargetInstance) {
        if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
        }
        
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());       
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("RESUME Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
            }            
            return;
        }
        HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());
        
        // Update the state of the allocated Target Instance
        aTargetInstance.setState(TargetInstance.STATE_RUNNING);       
        targetInstanceManager.save(aTargetInstance);
        
        agent.resume(aTargetInstance.getJobName());     
        
        // When profile overrides need updating we should also reset the bandwidth restrictions
        sendBandWidthRestrictions();
    }

    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#abort(TargetInstance)
     */
    public void abort(TargetInstance aTargetInstance) {
        if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
        }
        
        // Update the state of the allocated Target Instance
        aTargetInstance.setState(TargetInstance.STATE_ABORTED);       
        targetInstanceDao.save(aTargetInstance);
        
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());  
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("ABORT Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
            }
        }
        else {
        	HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());
        	try {				
				agent.abort(aTargetInstance.getJobName());
			} 
        	catch (RuntimeException e) {
        		if (log.isWarnEnabled()) {
                    log.warn("ABORT Failed. Failed Abort the Job " + aTargetInstance.getJobName() + " on the Harvest Agent " + agent.getName() + ".");
                }
			}    
        }
                
        sendBandWidthRestrictions();
    }

    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#stop(TargetInstance)
     */
    public void stop(TargetInstance aTargetInstance) {
        if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the harvest command.");
        }
        
        HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        if (status == null) {
            if (log.isWarnEnabled()) {
                log.warn("STOP Failed. Failed to find the Harvest Agent for the Job " + aTargetInstance.getJobName() + ".");
            }
            return;
        }
        HarvestAgent agent = harvestAgentFactory.getHarvestAgent(status.getHost(), status.getPort());
                
        agent.stop(aTargetInstance.getJobName());           
    }

    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#pauseAll()
     */
    public void pauseAll() {
    	HarvestAgentStatusDTO agentDTO = null;
    	HarvestAgent agent = null;
    	Iterator it = harvestAgents.values().iterator();
        while (it.hasNext()) {
            agentDTO = (HarvestAgentStatusDTO) it.next();
            if (agentDTO.getHarvesterStatus() != null && !agentDTO.getHarvesterStatus().isEmpty()) {
            	agent = harvestAgentFactory.getHarvestAgent(agentDTO.getHost(), agentDTO.getPort());
            	agent.pauseAll();
            }
        }		
	}

    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#resumeAll()
     */
	public void resumeAll() {
		HarvestAgentStatusDTO agentDTO = null;
    	HarvestAgent agent = null;
    	Iterator it = harvestAgents.values().iterator();
        while (it.hasNext()) {
            agentDTO = (HarvestAgentStatusDTO) it.next();
            if (agentDTO.getHarvesterStatus() != null && !agentDTO.getHarvesterStatus().isEmpty()) {
            	agent = harvestAgentFactory.getHarvestAgent(agentDTO.getHost(), agentDTO.getPort());
            	agent.resumeAll();
            }
        }	
	}
	
    /**
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#pauseQueue()
     */
    public void pauseQueue()
    {
    	queuePaused = true;
    }
    
    /** 
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#resumeQueue()
     */
    public void resumeQueue()
    {
    	queuePaused = false;
    }
    
    /** 
     * @see org.webcurator.core.harvester.coordinator.HarvestCoordinator#isQueuePaused()
     */
    public boolean isQueuePaused()
    {
    	return queuePaused;
    }
	
    /** @see HarvestCoordinator#processSchedule(). */
	public void processSchedule() {
		long now = System.currentTimeMillis();
		List<QueuedTargetInstanceDTO> theQueue = targetInstanceDao.getQueue();
		if (log.isDebugEnabled()) {
			log.debug("Start: Processing " + theQueue.size() + " entries from the queue.");
		}
				
		QueuedTargetInstanceDTO ti = null;
		Iterator<QueuedTargetInstanceDTO> it = theQueue.iterator();
		while (it.hasNext()) {
			ti = it.next();	
			harvestOrQueue(ti);			
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Finished: Processing " + theQueue.size() + " entries from the queue. Took " + (System.currentTimeMillis() - now) + "ms");
		}
	}	
	
	/**
	 * Run the checks to see if the target instance can be harvested or if it must be queued.
	 * If harvest is possible and there is a harvester available then allocate it.
	 * @param aTargetInstance the target instance to harvest
	 */
	public void harvestOrQueue(QueuedTargetInstanceDTO aTargetInstance) {		
		TargetInstance ti = null;
		boolean approved = false;
		if (TargetInstance.STATE_SCHEDULED.equals(aTargetInstance.getState())) {
			ti = targetInstanceDao.load(aTargetInstance.getOid());
			ti = targetInstanceDao.populate(ti);
			approved = isTargetApproved(ti);
		}
		else {
			approved = true;
		}
		
		if (approved) {
			boolean processed = false;					
			while (!processed) {				
				// Check to see what harvester resource is available
				HarvestAgentStatusDTO agent = getHarvester(aTargetInstance.getAgencyName());
				
				if (!queuePaused &&
					isMiniumBandwidthAvailable(aTargetInstance) && 
					agent != null) {					
					synchronized (agent) {
						// allocate the target instance to the agent
						if (ti == null) {
							ti = targetInstanceDao.load(aTargetInstance.getOid());
							ti = targetInstanceDao.populate(ti);
						}
						
						try {
							if(!TargetInstance.STATE_QUEUED.equals(ti.getState())) {
								prepareHarvest(ti);
							}
							_harvest(ti, agent);							
							agent.setInTransition(true);							
							processed = true;
						} 
						catch (Throwable e) {
							if (log.isWarnEnabled()) {
								log.warn("Failed to allocate harvest to agent " + agent.getName() + ": " + e.getMessage(), e);
							}
							
							harvestAgents.remove(agent.getName());							
						}
					}	
				}
				else {					
					processed = true;
					// if not already queued set the target instance to the queued state.
					if (!aTargetInstance.getState().equals(TargetInstance.STATE_QUEUED)) {
						if (ti == null) {
							ti = targetInstanceDao.load(aTargetInstance.getOid());
							ti = targetInstanceDao.populate(ti);
						}
						
						// Prepare the harvest for the queue.
						prepareHarvest(ti);
						
						ti.setState(TargetInstance.STATE_QUEUED);					
						targetInstanceDao.save(ti);
                        inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_INSTANCE_QUEUED, ti); 
					}							
				}
			}
		}
	}
	
	/**
	 * Check that the target that the instance belongs to is approved and if not dont harvest.
	 * @param aTargetInstance the target instance whos target should be checked.
	 * @return flag to indicat approval
	 */
	private boolean isTargetApproved(TargetInstance aTargetInstance) {		
		// Check permissions if none defer the target instance and send and notification
		if (!targetManager.isTargetHarvestable(aTargetInstance)) {				
			// Defer the schedule 24 hours and notifiy the owner.
			Calendar cal = Calendar.getInstance();
			cal.setTime(aTargetInstance.getScheduledTime());
			cal.add(Calendar.DATE, 1);
			
			aTargetInstance.setScheduledTime(cal.getTime());
			targetInstanceDao.save(aTargetInstance);
			
			if (log.isInfoEnabled()) {
				log.info("The target " + aTargetInstance.getTarget().getName() + " is not apporoved for harvest and has been defered 24 hours.");
			}
            inTrayManager.generateNotification(aTargetInstance.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_INSTANCE_RESCHEDULED, aTargetInstance);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Return the next harvest agent to allocate a target instance to.
	 * @param aTargetInstance the target instance to assign
	 * @return the harvest agent
	 */
	private HarvestAgentStatusDTO getHarvester(String aAgencyName) {		
		HarvestAgentStatusDTO selectedAgent = null;
		
		HarvestAgentStatusDTO agent = null;
		Iterator<HarvestAgentStatusDTO> it = harvestAgents.values().iterator();		
		while (it.hasNext()) {			
			agent = (HarvestAgentStatusDTO) it.next();
			if (agent.getAllowedAgencies() == null 
				|| agent.getAllowedAgencies().isEmpty() 
				|| agent.getAllowedAgencies().contains(aAgencyName)) {				
				if (selectedAgent == null || 
					agent.getHarvesterStatusCount() < selectedAgent.getHarvesterStatusCount()) {
					if ( !agent.getMemoryWarning() &&
						 !agent.isInTransition() &&
						 agent.getHarvesterStatusCount() < agent.getMaxHarvests()) {
						selectedAgent = agent;
					}				
				}
			}
		}		
		
		return selectedAgent;
	}
	
	/**
	 * Check to see that at least the minimum amount of bandwith can be allocated to
	 * all the running target instances assuming that this target instance is 
	 * allocated to a harvest agent
	 * @param aTargetInstance the target instances that may be allocated
	 * @return true if the minimum bandwidth will be available.
	 */
	private boolean isMiniumBandwidthAvailable(QueuedTargetInstanceDTO aTargetInstance) {
		if (null == aTargetInstance) {
			throw new WCTRuntimeException("The Target Instance passed in was null.");
		}
	
		if (getCurrentGlobalMaxBandwidth() < minimumBandwidth) {
			return false;
		}
		
		TargetInstance ti = targetInstanceDao.load(aTargetInstance.getOid());
		ti = targetInstanceDao.populate(ti);
        
        return isMiniumBandwidthAvailable(ti);
	}
	
	/** @see HarvestCoordinator#isMiniumBandwidthAvailable(TargetInstance) . */
	public boolean isMiniumBandwidthAvailable(TargetInstance aTargetInstance) {
		if (null == aTargetInstance) {
			throw new WCTRuntimeException("The Target Instance passed in was null.");
		}
	
		if (getCurrentGlobalMaxBandwidth() < minimumBandwidth) {
			return false;
		}
		
		TargetInstance ti = targetInstanceDao.load(aTargetInstance.getOid());
		ti = targetInstanceDao.populate(ti);
		
		HashMap targetInstances = null;
		if (TargetInstance.STATE_PAUSED.equals(ti.getState())) {
			targetInstances = calculateBandwidthAllocation();			
		}
		else {
			targetInstances = calculateBandwidthAllocation(ti);
		}
		        
        if (ti.getBandwidthPercent() == null) {        	
            if (ti.getAllocatedBandwidth() < minimumBandwidth) {            
                // failure bandwidth setting is too low.                    
                return false;
            }
        }
        else {
            TargetInstance ati = null;
            Iterator it = targetInstances.values().iterator();
            while (it.hasNext()) {
                ati = (TargetInstance) it.next();
                if (ati.getBandwidthPercent() == null) {                	
                    if (ati.getAllocatedBandwidth() < minimumBandwidth) {                    	
                        // failure bandwidth setting is too low.                            
                        return false;
                    }
                }
            }
        } 
        
        return true;
	}

	/**
	 * @see HarvestCoordinator#listLogFiles(TargetInstance)
	 */
	public List<String> listLogFiles(TargetInstance aTargetInstance) {
        if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the listLogFiles command.");
        }
        
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("list Log Files Failed. Failed to find the Log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		
        		return new ArrayList<String>();
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());        	        
        	return logReader.listLogFiles(aTargetInstance.getJobName());
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();         	        
        	return logReader.listLogFiles(aTargetInstance.getJobName());
        }
	}
	
	/**
	 * @see HarvestCoordinator#listLogFileAttributes(TargetInstance)
	 */
	public LogFilePropertiesDTO[] listLogFileAttributes(TargetInstance aTargetInstance) {
        
		if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the listLogFileAttributes command.");
        }
        
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("listLogFileAttributes Failed. Failed to find the Log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		LogFilePropertiesDTO[] empty = new LogFilePropertiesDTO[0];
        		return empty;
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());        	        
        	return logReader.listLogFileAttributes(aTargetInstance.getJobName());
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();         	        
        	return logReader.listLogFileAttributes(aTargetInstance.getJobName());
        }
	}

	/**
	 * @see HarvestCoordinator#tailLog(TargetInstance, String, int)
	 */
	public String[] tailLog(TargetInstance aTargetInstance, String aFileName, int aNoOfLines) {
		if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the tailLog command.");
        }
        
		String[] data = {""};
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("Tail Log Files Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		        		
        		return data;
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());              
        	return logReader.tail(aTargetInstance.getJobName(), aFileName, aNoOfLines);
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
        	return logReader.tail(aTargetInstance.getJobName(), aFileName, aNoOfLines);
        }
	}

	/**
	 * @see HarvestCoordinator#countLogLines(TargetInstance, String)
	 */
	public Integer countLogLines(TargetInstance aTargetInstance, String aFileName) {
		if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the countLogLines command.");
        }
        
		Integer count = 0;
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("Count Log Lines Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		        		
        		return count;
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());              
        	return logReader.countLines(aTargetInstance.getJobName(), aFileName);
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
        	return logReader.countLines(aTargetInstance.getJobName(), aFileName);
        }
	}

	/**
	 * @see HarvestCoordinator#headLog(TargetInstance, String, int)
	 */
	public String[] headLog(TargetInstance aTargetInstance, String aFileName, int aNoOfLines) {
		if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the headLog command.");
        }
        
		String[] data = {""};
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("Head Log Files Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		        		
        		return data;
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());              
        	return logReader.get(aTargetInstance.getJobName(), aFileName, 1, aNoOfLines);
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
        	return logReader.get(aTargetInstance.getJobName(), aFileName, 1, aNoOfLines);
        }
	}

	/**
	 * @see HarvestCoordinator#getLog(TargetInstance, String, int, int)
	 */
	public String[] getLog(TargetInstance aTargetInstance, String aFileName, int aStartLine, int aNoOfLines) {
		if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the getLog command.");
        }
        
		String[] data = {""};
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("Get Log Files Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		        		
        		return data;
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());              
        	return logReader.get(aTargetInstance.getJobName(), aFileName, aStartLine, aNoOfLines);
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
        	return logReader.get(aTargetInstance.getJobName(), aFileName, aStartLine, aNoOfLines);
        }
	}

	/**
	 * @see HarvestCoordinator#getFirstLogLineBeginning(TargetInstance, String, String)
	 */
    public Integer getFirstLogLineBeginning(TargetInstance aTargetInstance, String aFileName, String match)
    {
		if (aTargetInstance == null) {
	        throw new WCTRuntimeException("A null target instance was provided to the getFirstLogLineBeginning command.");
	    }
	    
	    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
	    	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
	    	// If we are harvesting then get the log files from the harvester
	    	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
	    	if (status == null) {
	    		if (log.isWarnEnabled()) {
	    			log.warn("Get First Log Line Beginning failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
	    		}
	    		        		
	    		return new Integer(0);
	    	}
	    	
	    	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
	    	return logReader.findFirstLineBeginning(aTargetInstance.getJobName(), aFileName, match);
	    }
	    else {
	    	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
	    	return logReader.findFirstLineBeginning(aTargetInstance.getJobName(), aFileName, match);
	    }
	}

	/**
	 * @see HarvestCoordinator#getFirstLogLineContaining(TargetInstance, String, String)
	 */
    public Integer getFirstLogLineContaining(TargetInstance aTargetInstance, String aFileName, String match)
    {
		if (aTargetInstance == null) {
	        throw new WCTRuntimeException("A null target instance was provided to the getFirstLogLineContaining command.");
	    }
	    
	    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
	    	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
	    	// If we are harvesting then get the log files from the harvester
	    	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
	    	if (status == null) {
	    		if (log.isWarnEnabled()) {
	    			log.warn("Get First Log Line Containing failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
	    		}
	    		        		
	    		return new Integer(0);
	    	}
	    	
	    	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
	    	return logReader.findFirstLineContaining(aTargetInstance.getJobName(), aFileName, match);
	    }
	    else {
	    	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
	    	return logReader.findFirstLineContaining(aTargetInstance.getJobName(), aFileName, match);
	    }
	}

	/**
	 * @see HarvestCoordinator#getFirstLogLineAfterTimeStamp(TargetInstance, String, Long)
	 */
    public Integer getFirstLogLineAfterTimeStamp(TargetInstance aTargetInstance, String aFileName, Long timestamp)
    {
		if (aTargetInstance == null) {
	        throw new WCTRuntimeException("A null target instance was provided to the getFirstLogLineAfterTimeStamp command.");
	    }
	    
	    if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
	    	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
	    	// If we are harvesting then get the log files from the harvester
	    	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
	    	if (status == null) {
	    		if (log.isWarnEnabled()) {
	    			log.warn("Get First Log Line After Timestamp failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
	    		}
	    		        		
	    		return new Integer(0);
	    	}
	    	
	    	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
	    	return logReader.findFirstLineAfterTimeStamp(aTargetInstance.getJobName(), aFileName, timestamp);
	    }
	    else {
	    	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
	    	return logReader.findFirstLineAfterTimeStamp(aTargetInstance.getJobName(), aFileName, timestamp);
	    }
	}
	
	/**
	 * @see HarvestCoordinator#getLogLinesByRegex(TargetInstance, String, int, String)
	 */
	public String[] getLogLinesByRegex(TargetInstance aTargetInstance, String aFileName, int aNoOfLines, String aRegex, boolean prependLineNumbers) {
		if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the regex command.");
        }
        
		String[] data = {""};
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("Get log lines by regex failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		        		
        		return data;
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());
        	return logReader.getByRegExpr(aTargetInstance.getJobName(), aFileName, aRegex, "zzzzzzzzz", prependLineNumbers, 0, aNoOfLines);
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
        	return logReader.getByRegExpr(aTargetInstance.getJobName(), aFileName, aRegex, "zzzzzzzz", prependLineNumbers, 0, aNoOfLines);
        }
	}

	/**
	 * @see HarvestCoordinator#getHopPath(TargetInstance, String, String)
	 */
	public String[] getHopPath(TargetInstance aTargetInstance, String aFileName, String aUrl) {
		if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the getHopPath command.");
        }
        
		String[] data = {""};
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("Get Log Files Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		        		
        		return data;
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());              
        	return logReader.getHopPath(aTargetInstance.getJobName(), aTargetInstance.getHarvestResult(1).getOid().toString(), aFileName, aUrl);
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
        	return logReader.getHopPath(aTargetInstance.getJobName(), aTargetInstance.getHarvestResult(1).getOid().toString(), aFileName, aUrl);
        }
	}
	
    /** 
     * @see HarvestCoordinator#purgeDigitalAssets().
     */
	public void purgeDigitalAssets() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, daysBeforeDASPurge * -1);
		
		List<TargetInstance> tis = targetInstanceDao.findPurgeableTargetInstances(cal.getTime());
		if (log.isDebugEnabled()) {
			log.debug("Attempting to purge " + tis.size() + " harvests from the digital asset store.");
		}
		
		if (tis != null && !tis.isEmpty()) {			
			int index = 0;
			String[] tiNames = new String[tis.size()];
			for (TargetInstance ti : tis) {
				tiNames[index++] = ti.getJobName();
			}
			
			try {
				digitalAssetStoreFactory.getDAS().purge(tiNames);				
				for (TargetInstance ti : tis) {
					targetInstanceManager.purgeTargetInstance(ti);
				}
			} 
			catch (DigitalAssetStoreException e) {
				if (log.isErrorEnabled()) {
					log.error("Failed to complete the purge " + e.getMessage(), e);
				}
			}
		}		
	}
	
    /** 
     * @see HarvestCoordinator#purgeAbortedTargetInstances().
     */
	public void purgeAbortedTargetInstances() {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, daysBeforeAbortedTargetInstancePurge * -1);
		
		List<TargetInstance> tis = targetInstanceDao.findPurgeableAbortedTargetInstances(cal.getTime());
		if (log.isDebugEnabled()) {
			log.debug("Attempting to purge " + tis.size() + " aborted harvests from the system.");
		}
		
		if (tis != null && !tis.isEmpty()) {			
			int index = 0;
			String[] tiNames = new String[tis.size()];
			for (TargetInstance ti : tis) {
				tiNames[index++] = ti.getJobName();
			}
			
			HarvestAgentSOAPClient ha = new HarvestAgentSOAPClient();
			ha.setHost(harvestAgentConfig.getHost());
			ha.setPort(harvestAgentConfig.getPort());
			ha.setService(harvestAgentConfig.getHarvestAgentServiceName());
			
			try {
				ha.purgeAbortedTargetInstances(tiNames);				
			} 
			catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error("Failed to complete the purge of aborted ti data via HA: " + e.getMessage(), e);
				}
			}
			
			// call the same web-method on the DAS, to delete folders which
			// may have been created in error while a running harvest was in
			// transition from running to stopping to harvested.
			try {
				digitalAssetStoreFactory.getDAS().purgeAbortedTargetInstances(tiNames);				
			} 
			catch (DigitalAssetStoreException e) {
				if (log.isErrorEnabled()) {
					log.error("Failed to complete the purge of aborted ti data via DAS: " + e.getMessage(), e);
				}
			}
			
			try {
				for (TargetInstance ti : tis) {
					targetInstanceManager.purgeTargetInstance(ti);
				}
			} 
			catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error("Failed to set the purged flag on all of the eligible aborted TIs: " + e.getMessage(), e);
				}
			}

		}		
	}

	/**
	 * @param digitalAssetStoreFactory the digitalAssetStoreFactory to set
	 */
	public void setDigitalAssetStoreFactory( DigitalAssetStoreFactory digitalAssetStoreFactory) {
		this.digitalAssetStoreFactory = digitalAssetStoreFactory;
	}

	/**
	 * @param harvestAgentConfig the harvestAgentConfig to set
	 */
	public void setHarvestAgentConfig( HarvestAgentConfig harvestAgentConfig) {
		this.harvestAgentConfig = harvestAgentConfig;
	}

	/**
	 * @param targetManager the targetManager to set
	 */
	public void setTargetManager(TargetManager targetManager) {
		this.targetManager = targetManager;
	}

    public void setInTrayManager(InTrayManager inTrayManager) {
        this.inTrayManager = inTrayManager;
    }

	/**
	 * @param daysBeforeDASPurge the daysBeforeDASPurge to set
	 */
	public void setDaysBeforeDASPurge(int daysBeforeDASPurge) {
		this.daysBeforeDASPurge = daysBeforeDASPurge;
	}

	/**
	 * @param daysBeforeAbortedTargetInstancePurge the daysBeforeAbortedTargetInstancePurge to set
	 */
	public void setDaysBeforeAbortedTargetInstancePurge(int daysBeforeAbortedTargetInstancePurge) {
		this.daysBeforeAbortedTargetInstancePurge = daysBeforeAbortedTargetInstancePurge;
	}

	/**
	 * @param auditor The auditor to set.
	 */
	public void setAuditor(Auditor auditor) {
		this.auditor = auditor;
	}

	public File getLogfile(TargetInstance aTargetInstance, String aFilename) {
		if (aTargetInstance == null) {
            throw new WCTRuntimeException("A null target instance was provided to the tail command.");
        }
        
        if (aTargetInstance.getState().equals(TargetInstance.STATE_RUNNING)
        	|| aTargetInstance.getState().equals(TargetInstance.STATE_PAUSED)) {
        	// If we are harvesting then get the log files from the harvester
        	HarvestAgentStatusDTO status = getHarvestAgent(aTargetInstance.getJobName());        
        	if (status == null) {
        		if (log.isWarnEnabled()) {
        			log.warn("Tail Log Files Failed. Failed to find the log Reader for the Job " + aTargetInstance.getJobName() + ".");
        		}
        		        		
        		return null;
        	}
        	
        	LogReader logReader = harvestAgentFactory.getLogReader(status.getHost(), status.getPort());              
        	return logReader.retrieveLogfile(aTargetInstance.getJobName(), aFilename);
        }
        else {
        	// if not then check to see if the log files are available from the digital asset store.
        	LogReader logReader = digitalAssetStoreFactory.getLogReader();
        	return logReader.retrieveLogfile(aTargetInstance.getJobName(), aFilename);
        }
	}

	/**
	 * @param sipBuilder the sipBuilder to set
	 */
	public void setSipBuilder(SipBuilder sipBuilder) {
		this.sipBuilder = sipBuilder;
	}

	
	public void addToHarvestResult(Long harvestResultOid, ArcHarvestFileDTO ahf) {
		ArcHarvestResult ahr = (ArcHarvestResult) targetInstanceDao.getHarvestResult(harvestResultOid, false);
		ArcHarvestFile f = new ArcHarvestFile(ahf, ahr);
		targetInstanceDao.save(f);
	}
	
	public void addHarvestResources(Long harvestResultOid, Collection<HarvestResourceDTO> dtos) { 
		ArcHarvestResult ahr = (ArcHarvestResult) targetInstanceDao.getHarvestResult(harvestResultOid, false);
		Collection<ArcHarvestResource> resources = new ArrayList<ArcHarvestResource>(dtos.size());
		for(HarvestResourceDTO dto : dtos) { 
			resources.add(new ArcHarvestResource( (ArcHarvestResourceDTO) dto, ahr));
		}
		targetInstanceDao.saveAll(resources);
	}

	
	public Long createHarvestResult(HarvestResultDTO harvestResultDTO) {
		if(harvestResultDTO instanceof ArcHarvestResultDTO) {
			TargetInstance ti = targetInstanceDao.load(harvestResultDTO.getTargetInstanceOid());
			ArcHarvestResult result = new ArcHarvestResult((ArcHarvestResultDTO)harvestResultDTO, ti);
			ti.getHarvestResults().add(result);
			result.setState(HarvestResult.STATE_INDEXING);
			
			targetInstanceDao.save(result);
			return result.getOid();
		}
		else {
			throw new IllegalArgumentException("Only supports ArcHarvestResults");
		}
	}

	
	public void finaliseIndex(Long harvestResultOid) {
		ArcHarvestResult ahr = (ArcHarvestResult) targetInstanceDao.getHarvestResult(harvestResultOid, false);
		ahr.setState(0);
		triggerAutoQA(ahr);
		targetInstanceDao.save(ahr);
	}
	
	public void notifyAQAComplete(String aqaId)
	{
		log.debug("Received notifyAQAComplete for job("+aqaId+").");
		
		try
		{
			String[] ids = aqaId.split("-");
			long tiOid = Long.parseLong(ids[0]); 
			TargetInstance ti = targetInstanceDao.load(tiOid);
			int harvestNumber = Integer.parseInt(ids[1]);
			HarvestResult result = ti.getHarvestResult(harvestNumber);
			
			// Send a message.
			inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.NOTIFICATION_AQA_COMPLETE, result);
		}
		catch(Exception e)
		{
			log.error("Unable to notify AQA Id: "+aqaId, e);
		}
	}
	
	private void triggerAutoQA(ArcHarvestResult ahr)
	{
	    TargetInstance ti = ahr.getTargetInstance();

	    if(autoQAUrl != null && autoQAUrl.length() > 0 && ti.isUseAQA())
		{
		    GetMethod getMethod = new GetMethod(autoQAUrl);
		    
		    String primarySeed = "";
		    
		    //Might be nice to use the SeedHistory here, but as this won't necessarily be turned on, we can't
		    //use it reliably
		    Set<Seed> seeds = ti.getTarget().getSeeds();
		    Iterator<Seed> it = seeds.iterator();
		    while(it.hasNext())
		    {
		    	Seed seed = it.next();
		    	if(seed.isPrimary())
		    	{
		    		primarySeed = seed.getSeed();
		    		break;
		    	}
		    }
		    
		    NameValuePair[] params = {	new NameValuePair("targetInstanceId", ti.getOid().toString()), 
		    							new NameValuePair("harvestNumber", Integer.toString(ahr.getHarvestNumber())), 
		    							new NameValuePair("primarySeed", primarySeed)};
		    
		    getMethod.setQueryString(params);
		    HttpClient client = new HttpClient();
		    try 
		    {
		        int result = client.executeMethod(getMethod);
		        if(result != HttpURLConnection.HTTP_OK)
		        {
		        	log.error("Unable to initiate Auto QA. Response at "+autoQAUrl+" is "+result);
		        }
		    } 
		    catch(Exception e)
		    {
	        	log.error("Unable to initiate Auto QA.", e);
		    }
		}
	}

	public void removeIndexes(TargetInstance ti)
	{
		List<HarvestResult> results = ti.getHarvestResults();
		if(results != null)
		{
			Iterator<HarvestResult> it = results.iterator();
			while(it.hasNext())
			{
				HarvestResult hr = it.next();
				if(hr.getState() != HarvestResult.STATE_REJECTED)
				{
					//Rejected HRs have already had their indexes removed
					//The endorsing process should mean there is only one none rejected HR
					removeIndexes(hr);
				}
			}
		}
	}
	
	public void removeIndexes(HarvestResult hr)
	{
		DigitalAssetStore das = digitalAssetStoreFactory.getDAS();
		try {
			log.info("Attempting to remove indexes for TargetInstance "+hr.getTargetInstance().getOid()+" HarvestNumber "+hr.getHarvestNumber());
			das.initiateRemoveIndexes(new ArcHarvestResultDTO(hr.getOid(), hr.getTargetInstance().getOid(), hr.getCreationDate(), hr.getHarvestNumber(), hr.getProvenanceNote()));
		} catch (DigitalAssetStoreException e) {
			if (log.isErrorEnabled()) {
				log.error("Could not send initiateRemoveIndexes message to the DAS for TargetInstance "+hr.getTargetInstance().getOid()+" HarvestNumber "+hr.getHarvestNumber()+": " + e.getMessage(), e);
			}
		}
	}
	
	public void completeArchiving(Long targetInstanceOid, String archiveIID) {
		// Update the state.
		TargetInstance ti = targetInstanceDao.load(targetInstanceOid);
		ti.setArchiveIdentifier(archiveIID);
		ti.setArchivedTime(new Date());
		ti.setState(TargetInstance.STATE_ARCHIVED);
		targetInstanceManager.save(ti);
		
		//Remove any indexes
		removeIndexes(ti);
		
		// Send a message.
		inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.NOTIFICATION_ARCHIVE_SUCCESS, ti);
		
	}

	public void failedArchiving(Long targetInstanceOid, String message) {
		// Update the state.
		TargetInstance ti = targetInstanceDao.load(targetInstanceOid);
		ti.setState(TargetInstance.STATE_ENDORSED);
		targetInstanceManager.save(ti);
		
		log.error("Failed to archive - trying to send message");
		
		// Send a message.
		inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, 
				"subject.archived.failed", new Object[] {ti.getTarget().getName(), ti.getResourceName()}, 
				"message.archived.failed", new Object[] {ti.getTarget().getName(), ti.getResourceName(), message},
				ti, true);
		
	}

	/**
	 * @param targetInstanceManager the targetInstanceManager to set
	 */
	public void setTargetInstanceManager(TargetInstanceManager targetInstanceManager) {
		this.targetInstanceManager = targetInstanceManager;
	}

	public void setAutoQAUrl(String autoQAUrl) {
		this.autoQAUrl = autoQAUrl;
	}

	public String getAutoQAUrl() {
		return autoQAUrl;
	}

	
	
}