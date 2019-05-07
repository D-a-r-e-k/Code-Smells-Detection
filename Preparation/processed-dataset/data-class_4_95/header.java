void method0() { 
//Some variable are not initialised to any value because the   
//default java value was desired initial value.   
//'dev ' = development build not yet final  
//'beta' = test build all features  
private static final String VER = "1.4.7";
//change also in QSAdminMain  
private static final String NEW_LINE = "\r\n";
private String serverBanner;
private String clientAuthenticationHandlerString;
//v1.4.6  
private String clientEventHandlerString;
//v1.4.6  
private String clientExtendedEventHandlerString;
//v1.4.6  
private String clientCommandHandlerString;
private String clientObjectHandlerString;
//v1.2  
private String clientBinaryHandlerString;
//v1.4  
private String clientWriteHandlerString;
//v1.4.5  
private String clientDataString;
private Authenticator authenticator;
private ClientAuthenticationHandler clientAuthenticationHandler;
//v1.4.6  
private ClientEventHandler clientEventHandler;
//v1.4.6  
private ClientExtendedEventHandler clientExtendedEventHandler;
//v1.4.6  
private ClientCommandHandler clientCommandHandler;
private ClientObjectHandler clientObjectHandler;
//v1.2  
private ClientBinaryHandler clientBinaryHandler;
//v1.4  
private ClientWriteHandler clientWriteHandler;
//v1.4.5  
private ClientData clientData;
protected Class clientDataClass;
private int serverPort = 9876;
private Thread t;
//Main thread  
private ServerSocket server;
private String serverName = "QuickServer";
private long maxConnection = -1;
private int socketTimeout = 60 * 1000;
//1 min socket timeout  
private String maxConnectionMsg = "-ERR Server Busy. Max Connection Reached";
private String timeoutMsg = "-ERR Timeout";
private String maxAuthTryMsg = "-ERR Max Auth Try Reached";
private int maxAuthTry = 5;
//--v1.1  
private InetAddress ipAddr;
private boolean stopServer;
private Object[] storeObjects;
private QSAdminServer adminServer;
//--v1.2  
//Logger for QuickServer  
private static final Logger logger = Logger.getLogger(QuickServer.class.getName());
//Logger for the application using this QuickServer  
private Logger appLogger;
//for Service interface  
private long suspendMaxConnection;
//backup  
private String suspendMaxConnectionMsg;
//backup  
private int serviceState = Service.UNKNOWN;
//--v1.3  
private QuickServerConfig config = new QuickServerConfig();
private String consoleLoggingformatter;
private String consoleLoggingLevel = "INFO";
private ClientPool pool;
private ObjectPool clientHandlerPool;
private ObjectPool clientDataPool;
private DBPoolUtil dBPoolUtil;
//--v1.3.1  
private String loggingLevel = "INFO";
//--v1.3.2  
private boolean skipValidation = false;
private boolean communicationLogging = true;
//--v1.3.3  
private String securityManagerClass;
private AccessConstraintConfig accessConstraintConfig;
private ClassLoader classLoader;
private String applicationJarPath;
private ServerHooks serverHooks;
private ArrayList listOfServerHooks;
//--v1.4.0  
private Secure secure;
private BasicServerConfig basicConfig = config;
private SSLContext sslc;
private KeyManager km[] = null;
private TrustManager tm[] = null;
private boolean runningSecure = false;
private SecureStoreManager secureStoreManager = null;
private Exception exceptionInRun = null;
//--v1.4.5  
private ServerSocketChannel serverSocketChannel;
private Selector selector;
private boolean blockingMode = true;
private ObjectPool byteBufferPool;
private java.util.Date lastStartTime;
private ClientIdentifier clientIdentifier;
private GhostSocketReaper ghostSocketReaper;
private PoolManager poolManager;
private QSObjectPoolMaker qsObjectPoolMaker;
//--v1.4.6  
private DataMode defaultDataModeIN = DataMode.STRING;
private DataMode defaultDataModeOUT = DataMode.STRING;
//-v1.4.7  
private Throwable serviceError;
private Map registerChannelRequestMap;
}
