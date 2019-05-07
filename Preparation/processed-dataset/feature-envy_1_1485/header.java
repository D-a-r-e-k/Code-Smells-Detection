void method0() { 
private static Logger logger = Logger.getLogger(RegexpHTMLLinkExtractor.class.getName());
boolean honorRobots = true;
boolean extractInlineCss = true;
boolean extractInlineJs = true;
protected LinkedList<Link> next = new LinkedList<Link>();
protected Matcher tags;
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
static final String RELEVANT_TAG_EXTRACTOR = "(?is)<(?:((script[^>]*+)>.*?</script)|((style[^>]*+)>[^<]*+</style)|(((meta)|(?:\\w+))\\s+[^>]*+)|(!--.*?--))>";
// this pattern extracts attributes from any open-tag innards 
// matched by the above. attributes known to be URIs of various 
// sorts are matched specially 
static final String EACH_ATTRIBUTE_EXTRACTOR = "(?is)\\s((href)|(action)|(on\\w*)" + "|((?:src)|(?:lowsrc)|(?:background)|(?:cite)|(?:longdesc)" + "|(?:usemap)|(?:profile)|(?:datasrc)|(?:for))" + "|(codebase)|((?:classid)|(?:data))|(archive)|(code)" + "|(value)|([-\\w]+))" + "\\s*=\\s*" + "(?:(?:\"(.*?)(?:\"|$))" + "|(?:'(.*?)(?:'|$))" + "|(\\S+))";
// groups: 
// 1: attribute name 
// 2: HREF - single URI relative to doc base, or occasionally javascript: 
// 3: ACTION - single URI relative to doc base, or occasionally javascript: 
// 4: ON[WHATEVER] - script handler 
// 5: SRC,LOWSRC,BACKGROUND,CITE,LONGDESC,USEMAP,PROFILE,DATASRC, or FOR 
//    single URI relative to doc base 
// 6: CODEBASE - a single URI relative to doc base, affecting other 
//    attributes 
// 7: CLASSID, DATA - a single URI relative to CODEBASE (if supplied) 
// 8: ARCHIVE - one or more space-delimited URIs relative to CODEBASE 
//    (if supplied) 
// 9: CODE - a single URI relative to the CODEBASE (is specified). 
// 10: VALUE - often includes a uri path on forms 
// 11: any other attribute 
// 12: double-quote delimited attr value 
// 13: single-quote delimited attr value 
// 14: space-delimited attr value 
// much like the javascript likely-URI extractor, but 
// without requiring quotes -- this can indicate whether 
// an HTML tag attribute that isn't definitionally a 
// URI might be one anyway, as in form-tag VALUE attributes 
static final String LIKELY_URI_PATH = "(\\.{0,2}[^\\.\\n\\r\\s\"']*(\\.[^\\.\\n\\r\\s\"']+)+)";
static final String ESCAPED_AMP = "&amp;";
static final String AMP = "&";
static final String WHITESPACE = "\\s";
static final String CLASSEXT = ".class";
static final String APPLET = "applet";
static final String BASE = "base";
static final String LINK = "link";
static final String JAVASCRIPT = "(?i)^javascript:.*";
static final String NON_HTML_PATH_EXTENSION = "(?i)(gif)|(jp(e)?g)|(png)|(tif(f)?)|(bmp)|(avi)|(mov)|(mp(e)?g)" + "|(mp3)|(mp4)|(swf)|(wav)|(au)|(aiff)|(mid)";
}
