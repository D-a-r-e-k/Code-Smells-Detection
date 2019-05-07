void method0() { 
// the versionstring for this release  
private static final String VERSION = "1.3.20100406";
// different request-types may be inserted here (for further use)  
public static final int REQUEST_TYPE_HTTP = 1;
// the home directory will be stored here  
public static String BASE_PATH;
public static final long startupTime = System.currentTimeMillis();
// these fields are used for the configuration  
// default-values may be changed in checkForConfigVals()  
public volatile String SMILEY_SERVER;
public volatile boolean USE_SMILEY;
public volatile int SMILEY_PER_LINE;
public String[] ADMIN_HTTP_USERNAME, ADMIN_HTTP_PASSWORD, ADMIN_HTTP_SECLEVEL;
public String DEFAULT_CHARSET = "iso-8859-1", TIMEZONE, ADMIN_HTTP_ALLOWED, ADMIN_XMLRPC_ALLOWED, DEFAULT_TEMPLATESET, DEFAULT_MEMBERSHIP;
public long TOUCH_USER_DELAY, READER_MAX_IDLETIME, FILE_CHECK_INTERVAL, FLOOD_PROTECT_MILLIS, USER_TIMEOUT, USER_AWAY_TIMEOUT, USER_REMOVE_SCHEDULE_TIME, HOST_BAN_DURATION, VIP_TIMEOUT, VIP_AWAY_TIMEOUT, READER_TIMEOUT, LOGIN_TIMEOUT, PUNISH_DURATION;
public boolean ALLOW_EXTERNAL, DEBUG_TEMPLATESET, USE_HTTP11, USE_IP_BAN, THREAD_PER_READ, USE_TOKENSTORE, MD5_PASSWORDS, USE_MESSAGE_RENDER_CACHE, USE_TRAFFIC_MONITOR, USE_CENTRAL_REQUESTQUEUE, ALLOW_CHANGE_USERAGENT, STRICT_HOST_BINDING, CAN_DEL_LOGS, USE_FADECOLOR, USE_BGCOLOR, BLOCKED_NICK_AUTOHARDKICK, USE_PLUGINS;
public int READER_MAX_QUEUE, READER_MAX_QUEUE_USAGE, MAX_READERS, MAX_BAN_DURATION, MAX_FLOCK_DURATION, MAX_SU_BAN_DURATION, DEFAULT_BAN_DURATION, FLOOD_PROTECT_TOLERANC, FLOOD_BAN_DURATION, READBUFFER_SIZE, MAX_USERS, COLOR_CHANGE_INTERVAL, MESSAGE_FLOOD_INTERVAL, MAX_USERNAME_LENGTH, MAX_DIE_NUMBER, MAX_DIE_EYES, TCP_RECEIVE_BUFFER_WINDOW, LOG_QUEUE_SIZE, MAX_REQUESTS_PER_PROXY_IP, MAX_REQUESTS_PER_IP, MAX_SUUSERS_PER_STARTGROUP, INITIAL_RESPONSE_QUEUE, MAX_RESPONSE_QUEUE, MAX_GROUPNAME_LENGTH, MAX_GROUPTHEME_LENGTH, ADMIN_XMLRPC_PORT, TOOL_PROTECT_COUNTER, TOOL_PROTECT_TOLERANC, TOOL_BAN_DURATION, TOOL_PROTECT_MINMILLS, TOOL_PROTECT_MINCOUNTER, JOIN_PUNISHED_COUNTER, LOGFILE_DELDAYS, LOGFILE_DELHOUR, MESSAGE_FLOOD_LENGHT, MAX_MCALL_KEY, MAX_PMSTORE;
private Vector<InetAddress> adminHosts;
private Vector<String> tempAdmins;
public Vector<InetAddress> allowedLoginHosts;
public Vector<String> SERVER_NAME;
public StringBuffer COOKIE_DOMAIN;
public TemplateManager templatemanager = null;
public AuthManager auth;
public static Server srv = null;
private Hashtable<String, BanObject> banList;
private Hashtable<ActionstoreObject, String> storeList;
public InetAddress lh = null;
public Properties props;
public static Calendar cal = Calendar.getInstance();
private volatile boolean isRunning = true;
public Charset defaultCs = Charset.forName(DEFAULT_CHARSET);
public CharsetEncoder defaultCsEnc = defaultCs.newEncoder();
public static boolean TRACE_CREATE_AND_FINALIZE = false;
private Hashtable<String, String> tokenStore = new Hashtable<String, String>();
public long KEEP_ALIVE_TIMEOUT;
public String UNAME_PREFIX_GOD, UNAME_PREFIX_GUEST, UNAME_PREFIX_MODERATOR, UNAME_PREFIX_PUNISHED, UNAME_PREFIX_SU, UNAME_PREFIX_VIP, UNAME_SUFFIX_GOD, UNAME_SUFFIX_GUEST, UNAME_SUFFIX_MODERATOR, UNAME_SUFFIX_PUNISHED, UNAME_SUFFIX_SU, UNAME_SUFFIX_VIP;
public short FN_DEFAULT_MODE_FALSE = 0;
public short FN_DEFAULT_MODE_TRUE = 2;
public short COLOR_LOCK_MODE = 0;
// brightly =1, darkly=2  
public short COLOR_LOCK_LEVEL = 1, FADECOLOR_LOCK_LEVEL = -1;
public String MIN_BBC_FONT_RIGHT_ENTRACE, MIN_BBC_FONT_RIGHT_SEPA, MIN_BBC_B_RIGHT_ENTRACE, MIN_BBC_B_RIGHT_SEPA, MIN_BBC_U_RIGHT_ENTRACE, MIN_BBC_U_RIGHT_SEPA, MIN_BBC_I_RIGHT_ENTRACE, MIN_BBC_I_RIGHT_SEPA;
public boolean USE_BBC, BBC_CONVERT_GROUPNAME, BBC_CONVERT_GROUPTHEME;
public int MAX_BBCTAGS;
public HashMap<String, Object> pluginStore = new HashMap<String, Object>();
public IServerPlugin[] serverPlugin = null;
public HashMap<String, Object> allCommands = new HashMap<String, Object>();
public HashMap<String, Object> xmlRpcHandler = new HashMap<String, Object>();
/**********************************************************************************************
 * LOGGING (will be moved to an extra object...
 **********************************************************************************************/
public static String[] LOGFILE = { "console", "console", "console", "console", "console", "console", "console" };
public static final short MSG_CONFIG = 0;
public static final short MSG_AUTH = 1;
public static final short MSG_STATE = 2;
public static final short MSG_TRAFFIC = 3;
public static final short MSG_ERROR = 4;
public static final short MSG_MESSAGE = 5;
public static final short MSG_SEPAMESSAGE = 6;
public static final short LVL_HALT = 0;
public static final short LVL_MAJOR = 1;
public static final short LVL_MINOR = 2;
public static final short LVL_VERBOSE = 3;
public static final short LVL_VERY_VERBOSE = 4;
public static boolean DEBUG = false;
public static Short LOG_MASK[] = new Short[7];
/**********************************************************************************************
 * INTERFACE RELOADABLE
 **********************************************************************************************/
private long lastModified;
private File configFile;
public static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
public static SimpleDateFormat hourSDF = new SimpleDateFormat("HH");
public static SimpleDateFormat minuteSDF = new SimpleDateFormat("mm");
}
