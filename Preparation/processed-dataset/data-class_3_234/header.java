void method0() { 
private static final long serialVersionUID = 570384305871965843L;
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
public static final String ATTR_SNOOZE_DEACTIVATE_MS = "snooze-deactivate-ms";
public static Long DEFAULT_SNOOZE_DEACTIVATE_MS = new Long(5 * 60 * 1000);
// 5 minutes 
private static final Logger logger = Logger.getLogger(WorkQueueFrontier.class.getName());
/** whether to hold queues INACTIVE until needed for throughput */
public static final String ATTR_HOLD_QUEUES = "hold-queues";
protected static final Boolean DEFAULT_HOLD_QUEUES = new Boolean(true);
/** amount to replenish budget on each activation (duty cycle) */
public static final String ATTR_BALANCE_REPLENISH_AMOUNT = "balance-replenish-amount";
protected static final Integer DEFAULT_BALANCE_REPLENISH_AMOUNT = new Integer(3000);
/** whether to hold queues INACTIVE until needed for throughput */
public static final String ATTR_ERROR_PENALTY_AMOUNT = "error-penalty-amount";
protected static final Integer DEFAULT_ERROR_PENALTY_AMOUNT = new Integer(100);
/** total expenditure to allow a queue before 'retiring' it  */
public static final String ATTR_QUEUE_TOTAL_BUDGET = "queue-total-budget";
protected static final Long DEFAULT_QUEUE_TOTAL_BUDGET = new Long(-1);
/** cost assignment policy to use (by class name) */
public static final String ATTR_COST_POLICY = "cost-policy";
protected static final String DEFAULT_COST_POLICY = UnitCostAssignmentPolicy.class.getName();
/** target size of ready queues backlog */
public static final String ATTR_TARGET_READY_QUEUES_BACKLOG = "target-ready-backlog";
protected static final Integer DEFAULT_TARGET_READY_QUEUES_BACKLOG = new Integer(50);
/** those UURIs which are already in-process (or processed), and
     thus should not be rescheduled */
protected transient UriUniqFilter alreadyIncluded;
/** All known queues.
     */
protected transient ObjectIdentityCache<String, WorkQueue> allQueues = null;
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
protected Bag inProcessQueues = BagUtils.synchronizedBag(new HashBag());
// of ClassKeyQueue 
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
private static final long DEFAULT_WAIT = 1000;
// 1 second 
/** a policy for assigning 'cost' values to CrawlURIs */
private transient CostAssignmentPolicy costAssignmentPolicy;
/** all policies available to be chosen */
String[] AVAILABLE_COST_POLICIES = new String[] { ZeroCostAssignmentPolicy.class.getName(), UnitCostAssignmentPolicy.class.getName(), WagCostAssignmentPolicy.class.getName(), AntiCalendarCostAssignmentPolicy.class.getName() };
// 
// Reporter implementation 
// 
public static String STANDARD_REPORT = "standard";
public static String ALL_NONEMPTY = "nonempty";
public static String ALL_QUEUES = "all";
protected static String[] REPORTS = { STANDARD_REPORT, ALL_NONEMPTY, ALL_QUEUES };
}
