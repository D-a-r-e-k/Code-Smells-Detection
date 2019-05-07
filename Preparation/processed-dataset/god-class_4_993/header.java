void method0() { 
private static final String ATTR_SPAMFILTER_SCORE = "spamfilter.score";
private static final String REASON_REGEXP = "Regexp";
private static final String REASON_IP_BANNED_TEMPORARILY = "IPBannedTemporarily";
private static final String REASON_BOT_TRAP = "BotTrap";
private static final String REASON_AKISMET = "Akismet";
private static final String REASON_TOO_MANY_URLS = "TooManyUrls";
private static final String REASON_SIMILAR_MODIFICATIONS = "SimilarModifications";
private static final String REASON_TOO_MANY_MODIFICATIONS = "TooManyModifications";
private static final String REASON_UTF8_TRAP = "UTF8Trap";
private static final String LISTVAR = "spamwords";
/** The filter property name for specifying the page which contains the list of spamwords.
     *  Value is <tt>{@value}</tt>. */
public static final String PROP_WORDLIST = "wordlist";
/** The filter property name for the page to which you are directed if Herb rejects your
     *  edit.  Value is <tt>{@value}</tt>. */
public static final String PROP_ERRORPAGE = "errorpage";
/** The filter property name for specifying how many changes is any given IP address
     *  allowed to do per minute.  Value is <tt>{@value}</tt>.
     */
public static final String PROP_PAGECHANGES = "pagechangesinminute";
/** The filter property name for specifying how many similar changes are allowed
     *  before a host is banned.  Value is <tt>{@value}</tt>.
     */
public static final String PROP_SIMILARCHANGES = "similarchanges";
/** The filter property name for specifying how long a host is banned.  Value is <tt>{@value}</tt>.*/
public static final String PROP_BANTIME = "bantime";
/** The filter property name for the attachment containing the blacklist.  Value is <tt>{@value}</tt>.*/
public static final String PROP_BLACKLIST = "blacklist";
/** The filter property name for specifying how many URLs can any given edit contain.  
     *  Value is <tt>{@value}</tt> */
public static final String PROP_MAXURLS = "maxurls";
/** The filter property name for specifying the Akismet API-key.  Value is <tt>{@value}</tt>. */
public static final String PROP_AKISMET_API_KEY = "akismet-apikey";
/** The filter property name for specifying whether authenticated users should be ignored. Value is <tt>{@value}</tt>. */
public static final String PROP_IGNORE_AUTHENTICATED = "ignoreauthenticated";
/** The filter property name for specifying which captcha technology should be used. Value is <tt>{@value}</tt>. */
public static final String PROP_CAPTCHA = "captcha";
/** The filter property name for specifying which filter strategy should be used.  Value is <tt>{@value}</tt>. */
public static final String PROP_FILTERSTRATEGY = "strategy";
/** The string specifying the "eager" strategy. Value is <tt>{@value}</tt>. */
public static final String STRATEGY_EAGER = "eager";
/** The string specifying the "score" strategy. Value is <tt>{@value}</tt>. */
public static final String STRATEGY_SCORE = "score";
private static final String URL_REGEXP = "(http://|https://|mailto:)([A-Za-z0-9_/\\.\\+\\?\\#\\-\\@=&;]+)";
private String m_forbiddenWordsPage = "SpamFilterWordList";
private String m_errorPage = "RejectedMessage";
private String m_blacklist = "SpamFilterWordList/blacklist.txt";
private PatternMatcher m_matcher = new Perl5Matcher();
private PatternCompiler m_compiler = new Perl5Compiler();
private Collection<Pattern> m_spamPatterns = null;
private Date m_lastRebuild = new Date(0L);
private static Logger c_spamlog = Logger.getLogger("SpamLog");
private static Logger log = Logger.getLogger(SpamFilter.class);
private Vector<Host> m_temporaryBanList = new Vector<Host>();
private int m_banTime = 60;
// minutes 
private Vector<Host> m_lastModifications = new Vector<Host>();
/**
     *  How many times a single IP address can change a page per minute?
     */
private int m_limitSinglePageChanges = 5;
/**
     *  How many times can you add the exact same string to a page?
     */
private int m_limitSimilarChanges = 2;
/**
     *  How many URLs can be added at maximum.
     */
private int m_maxUrls = 10;
private Pattern m_urlPattern;
private Akismet m_akismet;
private String m_akismetAPIKey = null;
private boolean m_useCaptcha = false;
/** The limit at which we consider something to be spam. */
private int m_scoreLimit = 1;
/**
     * If set to true, will ignore anyone who is in Authenticated role.
     */
private boolean m_ignoreAuthenticated = false;
private boolean m_stopAtFirstMatch = true;
private static String c_hashName;
private static long c_lastUpdate;
/** The HASH_DELAY value is a maximum amount of time that an user can keep
     *  a session open, because after the value has expired, we will invent a new
     *  hash field name.  By default this is {@value} hours, which should be ample
     *  time for someone.
     */
private static final long HASH_DELAY = 24;
private static final int REJECT = 0;
private static final int ACCEPT = 1;
private static final int NOTE = 2;
}
