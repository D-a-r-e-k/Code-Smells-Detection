void method0() { 
/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
protected static final String LOCK_TRIGGER_ACCESS = "TRIGGER_ACCESS";
protected static final String LOCK_JOB_ACCESS = "JOB_ACCESS";
protected static final String LOCK_CALENDAR_ACCESS = "CALENDAR_ACCESS";
protected static final String LOCK_STATE_ACCESS = "STATE_ACCESS";
protected static final String LOCK_MISFIRE_ACCESS = "MISFIRE_ACCESS";
/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
protected String dsName;
protected String tablePrefix = DEFAULT_TABLE_PREFIX;
protected boolean useProperties = false;
protected String instanceId;
protected String instanceName;
protected String delegateClassName;
protected Class delegateClass = StdJDBCDelegate.class;
protected HashMap calendarCache = new HashMap();
private DriverDelegate delegate;
private long misfireThreshold = 60000L;
// one minute 
private boolean dontSetAutoCommitFalse = false;
private boolean isClustered = false;
private boolean useDBLocks = false;
private boolean lockOnInsert = true;
private Semaphore lockHandler = null;
// set in initialize() method... 
private String selectWithLockSQL = null;
private long clusterCheckinInterval = 7500L;
private ClusterManager clusterManagementThread = null;
private MisfireHandler misfireHandler = null;
private ClassLoadHelper classLoadHelper;
private SchedulerSignaler schedSignaler;
protected int maxToRecoverAtATime = 20;
private boolean setTxIsolationLevelSequential = false;
private boolean acquireTriggersWithinLock = false;
private long dbRetryInterval = 10000;
private boolean makeThreadsDaemons = false;
private boolean threadsInheritInitializersClassLoadContext = false;
private ClassLoader initializersLoader = null;
private boolean doubleCheckLockMisfireHandler = true;
private final Logger log = LoggerFactory.getLogger(getClass());
private static long ftrCtr = System.currentTimeMillis();
protected ThreadLocal<Long> sigChangeForTxCompletion = new ThreadLocal<Long>();
//--------------------------------------------------------------------------- 
// Cluster management methods 
//--------------------------------------------------------------------------- 
protected boolean firstCheckIn = true;
protected long lastCheckin = System.currentTimeMillis();
}
