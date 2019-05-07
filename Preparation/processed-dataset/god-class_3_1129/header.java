void method0() { 
private static final long serialVersionUID = 240L;
private static final Logger log = LoggingManager.getLoggerForClass();
private static final Set<String> APPLIABLE_CONFIG_CLASSES = new HashSet<String>(Arrays.asList(new String[] { "org.apache.jmeter.config.gui.LoginConfigGui", "org.apache.jmeter.protocol.http.config.gui.HttpDefaultsGui", "org.apache.jmeter.config.gui.SimpleConfigGui", "org.apache.jmeter.protocol.http.gui.HeaderPanel", "org.apache.jmeter.protocol.http.gui.AuthPanel", "org.apache.jmeter.protocol.http.gui.CacheManagerGui", "org.apache.jmeter.protocol.http.gui.CookiePanel" }));
//+ JMX names - do not change 
public static final String ARGUMENTS = "HTTPsampler.Arguments";
// $NON-NLS-1$ 
public static final String AUTH_MANAGER = "HTTPSampler.auth_manager";
// $NON-NLS-1$ 
public static final String COOKIE_MANAGER = "HTTPSampler.cookie_manager";
// $NON-NLS-1$ 
public static final String CACHE_MANAGER = "HTTPSampler.cache_manager";
// $NON-NLS-1$ 
public static final String HEADER_MANAGER = "HTTPSampler.header_manager";
// $NON-NLS-1$ 
public static final String DOMAIN = "HTTPSampler.domain";
// $NON-NLS-1$ 
public static final String PORT = "HTTPSampler.port";
// $NON-NLS-1$ 
public static final String PROXYHOST = "HTTPSampler.proxyHost";
// $NON-NLS-1$ 
public static final String PROXYPORT = "HTTPSampler.proxyPort";
// $NON-NLS-1$ 
public static final String PROXYUSER = "HTTPSampler.proxyUser";
// $NON-NLS-1$ 
public static final String PROXYPASS = "HTTPSampler.proxyPass";
// $NON-NLS-1$ 
public static final String CONNECT_TIMEOUT = "HTTPSampler.connect_timeout";
// $NON-NLS-1$ 
public static final String RESPONSE_TIMEOUT = "HTTPSampler.response_timeout";
// $NON-NLS-1$ 
public static final String METHOD = "HTTPSampler.method";
// $NON-NLS-1$ 
/** This is the encoding used for the content, i.e. the charset name, not the header "Content-Encoding" */
public static final String CONTENT_ENCODING = "HTTPSampler.contentEncoding";
// $NON-NLS-1$ 
public static final String IMPLEMENTATION = "HTTPSampler.implementation";
// $NON-NLS-1$ 
public static final String PATH = "HTTPSampler.path";
// $NON-NLS-1$ 
public static final String FOLLOW_REDIRECTS = "HTTPSampler.follow_redirects";
// $NON-NLS-1$ 
public static final String AUTO_REDIRECTS = "HTTPSampler.auto_redirects";
// $NON-NLS-1$ 
public static final String PROTOCOL = "HTTPSampler.protocol";
// $NON-NLS-1$ 
static final String PROTOCOL_FILE = "file";
// $NON-NLS-1$ 
private static final String DEFAULT_PROTOCOL = HTTPConstants.PROTOCOL_HTTP;
public static final String URL = "HTTPSampler.URL";
// $NON-NLS-1$ 
/**
     * IP source to use - does not apply to Java HTTP implementation currently
     */
public static final String IP_SOURCE = "HTTPSampler.ipSource";
// $NON-NLS-1$ 
public static final String USE_KEEPALIVE = "HTTPSampler.use_keepalive";
// $NON-NLS-1$ 
public static final String DO_MULTIPART_POST = "HTTPSampler.DO_MULTIPART_POST";
// $NON-NLS-1$ 
public static final String BROWSER_COMPATIBLE_MULTIPART = "HTTPSampler.BROWSER_COMPATIBLE_MULTIPART";
// $NON-NLS-1$ 
public static final String CONCURRENT_DWN = "HTTPSampler.concurrentDwn";
// $NON-NLS-1$ 
public static final String CONCURRENT_POOL = "HTTPSampler.concurrentPool";
// $NON-NLS-1$ 
private static final String CONCURRENT_POOL_DEFAULT = "4";
// default for concurrent pool (do not change) 
//- JMX names 
public static final boolean BROWSER_COMPATIBLE_MULTIPART_MODE_DEFAULT = false;
// The default setting to be used (i.e. historic) 
private static final long KEEPALIVETIME = 0;
// for Thread Pool for resources but no need to use a special value? 
private static final long AWAIT_TERMINATION_TIMEOUT = JMeterUtils.getPropDefault("httpsampler.await_termination_timeout", 60);
// $NON-NLS-1$ // default value: 60 secs  
private static final boolean IGNORE_FAILED_EMBEDDED_RESOURCES = JMeterUtils.getPropDefault("httpsampler.ignore_failed_embedded_resources", false);
// $NON-NLS-1$ // default value: false 
public static final int CONCURRENT_POOL_SIZE = 4;
// Default concurrent pool size for download embedded resources 
public static final String DEFAULT_METHOD = HTTPConstants.GET;
// $NON-NLS-1$ 
// Supported methods: 
private static final String[] METHODS = { DEFAULT_METHOD, // i.e. GET 
HTTPConstants.POST, HTTPConstants.HEAD, HTTPConstants.PUT, HTTPConstants.OPTIONS, HTTPConstants.TRACE, HTTPConstants.DELETE, HTTPConstants.PATCH };
private static final List<String> METHODLIST = Collections.unmodifiableList(Arrays.asList(METHODS));
// @see mergeFileProperties 
// Must be private, as the file list needs special handling 
private static final String FILE_ARGS = "HTTPsampler.Files";
// $NON-NLS-1$ 
// MIMETYPE is kept for backward compatibility with old test plans 
private static final String MIMETYPE = "HTTPSampler.mimetype";
// $NON-NLS-1$ 
// FILE_NAME is kept for backward compatibility with old test plans 
private static final String FILE_NAME = "HTTPSampler.FILE_NAME";
// $NON-NLS-1$ 
/* Shown as Parameter Name on the GUI */
// FILE_FIELD is kept for backward compatibility with old test plans 
private static final String FILE_FIELD = "HTTPSampler.FILE_FIELD";
// $NON-NLS-1$ 
public static final String CONTENT_TYPE = "HTTPSampler.CONTENT_TYPE";
// $NON-NLS-1$ 
// IMAGE_PARSER now really means EMBEDDED_PARSER 
public static final String IMAGE_PARSER = "HTTPSampler.image_parser";
// $NON-NLS-1$ 
// Embedded URLs must match this RE (if provided) 
public static final String EMBEDDED_URL_RE = "HTTPSampler.embedded_url_re";
// $NON-NLS-1$ 
public static final String MONITOR = "HTTPSampler.monitor";
// $NON-NLS-1$ 
// Store MD5 hash instead of storing response 
private static final String MD5 = "HTTPSampler.md5";
// $NON-NLS-1$ 
/** A number to indicate that the port has not been set. */
public static final int UNSPECIFIED_PORT = 0;
public static final String UNSPECIFIED_PORT_AS_STRING = "0";
// $NON-NLS-1$ 
// TODO - change to use URL version? Will this affect test plans? 
/** If the port is not present in a URL, getPort() returns -1 */
public static final int URL_UNSPECIFIED_PORT = -1;
public static final String URL_UNSPECIFIED_PORT_AS_STRING = "-1";
// $NON-NLS-1$ 
protected static final String NON_HTTP_RESPONSE_CODE = "Non HTTP response code";
protected static final String NON_HTTP_RESPONSE_MESSAGE = "Non HTTP response message";
public static final String POST_BODY_RAW = "HTTPSampler.postBodyRaw";
// TODO - belongs elsewhere  
public static final boolean POST_BODY_RAW_DEFAULT = false;
private static final String ARG_VAL_SEP = "=";
// $NON-NLS-1$ 
private static final String QRY_SEP = "&";
// $NON-NLS-1$ 
private static final String QRY_PFX = "?";
// $NON-NLS-1$ 
protected static final int MAX_REDIRECTS = JMeterUtils.getPropDefault("httpsampler.max_redirects", 5);
// $NON-NLS-1$ 
protected static final int MAX_FRAME_DEPTH = JMeterUtils.getPropDefault("httpsampler.max_frame_depth", 5);
// $NON-NLS-1$ 
// Derive the mapping of content types to parsers 
private static final Map<String, String> parsersForType = new HashMap<String, String>();
// Not synch, but it is not modified after creation 
private static final String RESPONSE_PARSERS = // list of parsers 
JMeterUtils.getProperty("HTTPResponse.parsers");
//$NON-NLS-1$ 
// Control reuse of cached SSL Context in subsequent iterations 
private static final boolean USE_CACHED_SSL_CONTEXT = JMeterUtils.getPropDefault("https.use.cached.ssl.context", true);
// Bug 49083 
/** Whether to remove '/pathsegment/..' from redirects; default true */
private static final boolean REMOVESLASHDOTDOT = JMeterUtils.getPropDefault("httpsampler.redirect.removeslashdotdot", true);
private static final String HTTP_PREFIX = HTTPConstants.PROTOCOL_HTTP + "://";
// $NON-NLS-1$ 
private static final String HTTPS_PREFIX = HTTPConstants.PROTOCOL_HTTPS + "://";
// $NON-NLS-1$ 
// Bug 51939 
private static final boolean SEPARATE_CONTAINER = JMeterUtils.getPropDefault("httpsampler.separate.container", true);
}
