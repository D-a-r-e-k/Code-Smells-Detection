void method0() { 
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
}
