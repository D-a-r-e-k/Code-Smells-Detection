void method0() { 
/** The logger for the Target Manager */
private static Log log = LogFactory.getLog(TargetManagerImpl.class);
/** The DAO for saving targets. */
private TargetDAO targetDao = null;
/** The DAO for loading sites & permissions */
private SiteDAO siteDao = null;
/** The DAO for loading/saving annotations */
private AnnotationDAO annotationDAO = null;
/** The DAO for loading/saving Target Instances */
private TargetInstanceDAO targetInstanceDao = null;
/** The Authority Manager */
private AuthorityManager authMgr = null;
/** The TargetInstanceManager */
private TargetInstanceManager instanceManager = null;
/** The IntrayManager */
private InTrayManager intrayManager = null;
/** The Auditor */
private Auditor auditor;
/** Message Source */
private MessageSource messageSource;
/** Business object factory */
private BusinessObjectFactory businessObjectFactory;
/** The list of valid sub-group parent types */
private WCTTreeSet subGroupParentTypesList = null;
/** Whether to send notifications when group membership is updated. */
private boolean sendGroupUpdateNotifications = true;
private boolean allowMultiplePrimarySeeds = true;
private String subGroupTypeName = null;
}
