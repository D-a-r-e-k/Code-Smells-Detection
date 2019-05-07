void method0() { 
////////////////////////////////////////////////////////////////////////////////////////////// 
// PUBLIC CONSTANTS 
////////////////////////////////////////////////////////////////////////////////////////////// 
/* If you change any of the final values here, you'll need to recompile every class that uses them.
   * So some lost their "final" at some point, but mustn't be modified anyway.
   */
/** Current Jext's release. */
public static String RELEASE = "5.0 <Karsten/Tiger>";
/**
   * Last Jext's build number. It's used actually only for plugin dependencies, so
   * don't change it for simple bug-fix which don't bump the release number.*/
public static String BUILD = "05.00.01.00";
/** If true, Jext will delete user settings if this release is newer */
public static boolean DELETE_OLD_SETTINGS = true;
/** Debug mode(not final to avoid it being included in other .class files) */
public static boolean DEBUG = false;
/** Available new lines characters */
public static final String[] NEW_LINE = { "\r", "\n", "\r\n" };
/** Settings directory. */
public static final String SETTINGS_DIRECTORY = System.getProperty("user.home") + File.separator + ".jext" + File.separator;
/** Jext home directory. */
public static final String JEXT_HOME = System.getProperty("user.dir");
/** Jext server base port number. Used to load all Jext instances with only one JVM. **/
public static final int JEXT_SERVER_PORT = 49152;
////////////////////////////////////////////////////////////////////////////////////////////// 
// BEGINNING OF STATIC PART 
////////////////////////////////////////////////////////////////////////////////////////////// 
// STATIC FIELDS 
////////////////////////////////////////////////////////////////////////////////////////////// 
// modes 
public static ArrayList modes;
public static ArrayList modesFileFilters;
// selected language 
private static String language = "English";
private static ZipFile languagePack;
private static ArrayList languageEntries;
// GUI option to have, or not flat menus 
private static boolean flatMenus = true;
// GUI option to have non highlighted buttons 
private static boolean buttonsHighlight = true;
// server socket 
private static JextLoader jextLoader;
private static boolean isServerEnabled;
// plugins specific variables 
private static ArrayList plugins;
// user properties filename 
public static String usrProps;
// the splash screen 
private static SplashScreen splash;
// the properties files 
private static Properties props, defaultProps;
// contains all the instances of Jext 
// this is an object we synchronize on to avoid window being created concurrently or when is not ready 
// enough(for instance by JextLoader when we didn't call initProperties() yet). 
private static ArrayList instances = new ArrayList(5);
// contains all the actions 
private static HashMap actionHash;
// contains all the python actions 
private static HashMap pythonActionHash = new HashMap();
// auto check 
private static VersionCheck check;
// input handler 
private static DefaultInputHandler inputHandler;
// user properties file name 
private static final String USER_PROPS = SETTINGS_DIRECTORY + ".jext-props.xml";
// this property(set by loadInSingleJVMInstance), if true, says we must not show 
private static boolean runInBg = false;
private static boolean keepInMemory = false;
//the default value found during loading must be stored for the option dialog 
private static boolean defaultKeepInMemory = false;
// when the user runs jext -kill, we store this here and go to kill the server(see  
// loadInSingleJVMInstance) 
private static boolean goingToKill = false;
// the text area we pre-build if running in background, that will be shown when Jext is started 
// by the user(so it will be very fast!) 
private static JextFrame builtTextArea = null;
}
