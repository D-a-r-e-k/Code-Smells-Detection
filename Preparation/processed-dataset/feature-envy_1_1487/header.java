void method0() { 
private static final long serialVersionUID = 5855731422080471017L;
private static Logger logger = Logger.getLogger(ExtractorHTML.class.getName());
/**
     * Compiled relevant tag extractor.
     *
     * <p>
     * This pattern extracts either:
     * <li> (1) whole &lt;script&gt;...&lt;/script&gt; or
     * <li> (2) &lt;style&gt;...&lt;/style&gt; or
     * <li> (3) &lt;meta ...&gt; or
     * <li> (4) any other open-tag with at least one attribute
     * (eg matches "&lt;a href='boo'&gt;" but not "&lt;/a&gt;" or "&lt;br&gt;")
     * <p>
     * groups:
     * <li> 1: SCRIPT SRC=foo&gt;boo&lt;/SCRIPT
     * <li> 2: just script open tag
     * <li> 3: STYLE TYPE=moo&gt;zoo&lt;/STYLE
     * <li> 4: just style open tag
     * <li> 5: entire other tag, without '<' '>'
     * <li> 6: element
     * <li> 7: META
     * <li> 8: !-- comment --
     */
// version w/ less unnecessary backtracking 
private static final int MAX_ELEMENT_LENGTH = Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() + ".maxElementNameLength", "1024"));
static final String RELEVANT_TAG_EXTRACTOR = "(?is)<(?:((script[^>]*+)>.*?</script)" + // 1, 2 
"|((style[^>]*+)>.*?</style)" + // 3, 4 
"|(((meta)|(?:\\w{1," + MAX_ELEMENT_LENGTH + "}))\\s+[^>]*+)" + // 5, 6, 7 
"|(!--.*?--))>";
// 8  
//    version w/ problems with unclosed script tags  
//    static final String RELEVANT_TAG_EXTRACTOR = 
//    "(?is)<(?:((script.*?)>.*?</script)|((style.*?)>.*?</style)|(((meta)|(?:\\w+))\\s+.*?)|(!--.*?--))>"; 
//    // this pattern extracts 'href' or 'src' attributes from 
//    // any open-tag innards matched by the above 
//    static Pattern RELEVANT_ATTRIBUTE_EXTRACTOR = Pattern.compile( 
//     "(?is)(\\w+)(?:\\s+|(?:\\s.*?\\s))(?:(href)|(src))\\s*=(?:(?:\\s*\"(.+?)\")|(?:\\s*'(.+?)')|(\\S+))"); 
// 
//    // this pattern extracts 'robots' attributes 
//    static Pattern ROBOTS_ATTRIBUTE_EXTRACTOR = Pattern.compile( 
//     "(?is)(\\w+)\\s+.*?(?:(robots))\\s*=(?:(?:\\s*\"(.+)\")|(?:\\s*'(.+)')|(\\S+))"); 
private static final int MAX_ATTR_NAME_LENGTH = Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() + ".maxAttributeNameLength", "1024"));
// 1K;  
static final int MAX_ATTR_VAL_LENGTH = Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() + ".maxAttributeValueLength", "16384"));
// 16K;  
// TODO: perhaps cut to near MAX_URI_LENGTH 
// this pattern extracts attributes from any open-tag innards 
// matched by the above. attributes known to be URIs of various 
// sorts are matched specially 
static final String EACH_ATTRIBUTE_EXTRACTOR = "(?is)\\b((href)|(action)|(on\\w*)" + "|((?:src)|(?:lowsrc)|(?:background)|(?:cite)|(?:longdesc)" + "|(?:usemap)|(?:profile)|(?:datasrc))" + "|(codebase)|((?:classid)|(?:data))|(archive)|(code)" + "|(value)|(style)|(method)" + "|([-\\w]{1," + MAX_ATTR_NAME_LENGTH + "}))" + "\\s*=\\s*" + "(?:(?:\"(.{0," + MAX_ATTR_VAL_LENGTH + "}?)(?:\"|$))" + "|(?:'(.{0," + MAX_ATTR_VAL_LENGTH + "}?)(?:'|$))" + "|(\\S{1," + MAX_ATTR_VAL_LENGTH + "}))";
// 16 
// groups: 
// 1: attribute name 
// 2: HREF - single URI relative to doc base, or occasionally javascript: 
// 3: ACTION - single URI relative to doc base, or occasionally javascript: 
// 4: ON[WHATEVER] - script handler 
// 5: SRC,LOWSRC,BACKGROUND,CITE,LONGDESC,USEMAP,PROFILE, or DATASRC 
//    single URI relative to doc base 
// 6: CODEBASE - a single URI relative to doc base, affecting other 
//    attributes 
// 7: CLASSID, DATA - a single URI relative to CODEBASE (if supplied) 
// 8: ARCHIVE - one or more space-delimited URIs relative to CODEBASE 
//    (if supplied) 
// 9: CODE - a single URI relative to the CODEBASE (is specified). 
// 10: VALUE - often includes a uri path on forms 
// 11: STYLE - inline attribute style info 
// 12: METHOD - form GET/POST 
// 13: any other attribute 
// 14: double-quote delimited attr value 
// 15: single-quote delimited attr value 
// 16: space-delimited attr value 
static final String WHITESPACE = "\\s";
static final String CLASSEXT = ".class";
static final String APPLET = "applet";
static final String BASE = "base";
static final String LINK = "link";
static final String FRAME = "frame";
static final String IFRAME = "iframe";
public static final String ATTR_TREAT_FRAMES_AS_EMBED_LINKS = "treat-frames-as-embed-links";
public static final String ATTR_IGNORE_FORM_ACTION_URLS = "ignore-form-action-urls";
public static final String ATTR_EXTRACT_ONLY_FORM_GETS = "extract-only-form-gets";
/** whether to try finding links in Javscript; default true */
public static final String ATTR_EXTRACT_JAVASCRIPT = "extract-javascript";
public static final String EXTRACT_VALUE_ATTRIBUTES = "extract-value-attributes";
public static final String ATTR_IGNORE_UNEXPECTED_HTML = "ignore-unexpected-html";
protected long numberOfCURIsHandled = 0;
protected long numberOfLinksExtracted = 0;
static final String JAVASCRIPT = "(?i)^javascript:.*";
static final String NON_HTML_PATH_EXTENSION = "(?i)(gif)|(jp(e)?g)|(png)|(tif(f)?)|(bmp)|(avi)|(mov)|(mp(e)?g)" + "|(mp3)|(mp4)|(swf)|(wav)|(au)|(aiff)|(mid)";
}
