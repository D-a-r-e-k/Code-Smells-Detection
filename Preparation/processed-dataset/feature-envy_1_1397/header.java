void method0() { 
/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
public static final String QUARTZ_NS = "http://www.quartz-scheduler.org/xml/JobSchedulingData";
public static final String QUARTZ_SCHEMA_WEB_URL = "http://www.quartz-scheduler.org/xml/job_scheduling_data_1_8.xsd";
public static final String QUARTZ_XSD_PATH_IN_JAR = "org/quartz/xml/job_scheduling_data_1_8.xsd";
public static final String QUARTZ_XML_DEFAULT_FILE_NAME = "quartz_data.xml";
public static final String QUARTZ_SYSTEM_ID_JAR_PREFIX = "jar:";
/**
     * XML Schema dateTime datatype format.
     * <p>
     * See <a
     * href="http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#dateTime">
     * http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#dateTime</a>
     */
protected static final String XSD_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";
protected static final SimpleDateFormat dateFormat = new SimpleDateFormat(XSD_DATE_FORMAT);
/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
// pre-processing commands 
protected List<String> jobGroupsToDelete = new LinkedList<String>();
protected List<String> triggerGroupsToDelete = new LinkedList<String>();
protected List<Key> jobsToDelete = new LinkedList<Key>();
protected List<Key> triggersToDelete = new LinkedList<Key>();
// scheduling commands 
protected List<JobDetail> loadedJobs = new LinkedList<JobDetail>();
protected List<Trigger> loadedTriggers = new LinkedList<Trigger>();
// directives 
private boolean overWriteExistingData = true;
private boolean ignoreDuplicates = false;
protected Collection validationExceptions = new ArrayList();
protected ClassLoadHelper classLoadHelper;
protected List<String> jobGroupsToNeverDelete = new LinkedList<String>();
protected List<String> triggerGroupsToNeverDelete = new LinkedList<String>();
private DocumentBuilder docBuilder = null;
private XPath xpath = null;
private final Logger log = LoggerFactory.getLogger(getClass());
}
