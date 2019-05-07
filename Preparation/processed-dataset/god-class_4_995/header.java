void method0() { 
/** Name of the outlink image; relative path to the JSPWiki directory. */
private static final String OUTLINK_IMAGE = "images/out.png";
/** The value for anchor element <tt>class</tt> attributes when used
      * for wiki page (normal) links. The value is "wikipage". */
public static final String CLASS_WIKIPAGE = "wikipage";
/** The value for anchor element <tt>class</tt> attributes when used
      * for edit page links. The value is "createpage". */
public static final String CLASS_EDITPAGE = "createpage";
/** The value for anchor element <tt>class</tt> attributes when used
      * for interwiki page links. The value is "interwiki". */
public static final String CLASS_INTERWIKI = "interwiki";
protected static final int READ = 0;
protected static final int EDIT = 1;
protected static final int EMPTY = 2;
// Empty message 
protected static final int LOCAL = 3;
protected static final int LOCALREF = 4;
protected static final int IMAGE = 5;
protected static final int EXTERNAL = 6;
protected static final int INTERWIKI = 7;
protected static final int IMAGELINK = 8;
protected static final int IMAGEWIKILINK = 9;
protected static final int ATTACHMENT = 10;
private static Logger log = Logger.getLogger(JSPWikiMarkupParser.class);
private boolean m_isbold = false;
private boolean m_isitalic = false;
private boolean m_istable = false;
private boolean m_isPre = false;
private boolean m_isEscaping = false;
private boolean m_isdefinition = false;
private boolean m_isPreBlock = false;
/** Contains style information, in multiple forms. */
private Stack<Boolean> m_styleStack = new Stack<Boolean>();
// general list handling 
private int m_genlistlevel = 0;
private StringBuilder m_genlistBulletBuffer = new StringBuilder(10);
// stores the # and * pattern 
private boolean m_allowPHPWikiStyleLists = true;
private boolean m_isOpenParagraph = false;
/** Keeps image regexp Patterns */
private List<Pattern> m_inlineImagePatterns;
/** Parser for extended link functionality. */
private LinkParser m_linkParser = new LinkParser();
private PatternMatcher m_inlineMatcher = new Perl5Matcher();
/** Keeps track of any plain text that gets put in the Text nodes */
private StringBuilder m_plainTextBuf = new StringBuilder(20);
private Element m_currentElement;
/** Keep track of duplicate header names.  */
private Map<String, Integer> m_titleSectionCounter = new HashMap<String, Integer>();
/**
     *  This property defines the inline image pattern.  It's current value
     *  is jspwiki.translatorReader.inlinePattern
     */
public static final String PROP_INLINEIMAGEPTRN = "jspwiki.translatorReader.inlinePattern";
/** If true, consider CamelCase hyperlinks as well. */
public static final String PROP_CAMELCASELINKS = "jspwiki.translatorReader.camelCaseLinks";
/** If true, all hyperlinks are translated as well, regardless whether they
        are surrounded by brackets. */
public static final String PROP_PLAINURIS = "jspwiki.translatorReader.plainUris";
/** If true, all outward links (external links) have a small link image appended. */
public static final String PROP_USEOUTLINKIMAGE = "jspwiki.translatorReader.useOutlinkImage";
/** If true, all outward attachment info links have a small link image appended. */
public static final String PROP_USEATTACHMENTIMAGE = "jspwiki.translatorReader.useAttachmentImage";
/** If set to "true", all external links are tagged with 'rel="nofollow"' */
public static final String PROP_USERELNOFOLLOW = "jspwiki.translatorReader.useRelNofollow";
/** If true, then considers CamelCase links as well. */
private boolean m_camelCaseLinks = false;
/** If true, then generate special output for wysiwyg editing in certain cases */
private boolean m_wysiwygEditorMode = false;
/** If true, consider URIs that have no brackets as well. */
// FIXME: Currently reserved, but not used. 
private boolean m_plainUris = false;
/** If true, all outward links use a small link image. */
private boolean m_useOutlinkImage = true;
private boolean m_useAttachmentImage = true;
/** If true, allows raw HTML. */
private boolean m_allowHTML = false;
private boolean m_useRelNofollow = false;
private PatternCompiler m_compiler = new Perl5Compiler();
static final String WIKIWORD_REGEX = "(^|[[:^alnum:]]+)([[:upper:]]+[[:lower:]]+[[:upper:]]+[[:alnum:]]*|(http://|https://|mailto:)([A-Za-z0-9_/\\.\\+\\?\\#\\-\\@=&;~%]+))";
private PatternMatcher m_camelCaseMatcher = new Perl5Matcher();
private Pattern m_camelCasePattern;
private int m_rowNum = 1;
private Heading m_lastHeading = null;
/**
     *  The default inlining pattern.  Currently "*.png"
     */
public static final String DEFAULT_INLINEPATTERN = "*.png";
/**
     *  This list contains all IANA registered URI protocol
     *  types as of September 2004 + a few well-known extra types.
     *
     *  JSPWiki recognises all of them as external links.
     *
     *  This array is sorted during class load, so you can just dump
     *  here whatever you want in whatever order you want.
     */
static final String[] EXTERNAL_LINKS = { "http:", "ftp:", "https:", "mailto:", "news:", "file:", "rtsp:", "mms:", "ldap:", "gopher:", "nntp:", "telnet:", "wais:", "prospero:", "z39.50s", "z39.50r", "vemmi:", "imap:", "nfs:", "acap:", "tip:", "pop:", "dav:", "opaquelocktoken:", "sip:", "sips:", "tel:", "fax:", "modem:", "soap.beep:", "soap.beeps", "xmlrpc.beep", "xmlrpc.beeps", "urn:", "go:", "h323:", "ipp:", "tftp:", "mupdate:", "pres:", "im:", "mtqp", "smb:" };
private static final String INLINE_IMAGE_PATTERNS = "JSPWikiMarkupParser.inlineImagePatterns";
private static final String CAMELCASE_PATTERN = "JSPWikiMarkupParser.camelCasePattern";
private static final String[] CLASS_TYPES = { CLASS_WIKIPAGE, CLASS_EDITPAGE, "", "footnote", "footnoteref", "", "external", CLASS_INTERWIKI, "external", CLASS_WIKIPAGE, "attachment" };
/**
     *  This Comparator is used to find an external link from c_externalLinks.  It
     *  checks if the link starts with the other arraythingie.
     */
private static Comparator<String> c_startingComparator = new StartingComparator();
/**
     *  These are all of the HTML 4.01 block-level elements.
     */
private static final String[] BLOCK_ELEMENTS = { "address", "blockquote", "div", "dl", "fieldset", "form", "h1", "h2", "h3", "h4", "h5", "h6", "hr", "noscript", "ol", "p", "pre", "table", "ul" };
/**
     *  All elements that can be empty by the HTML DTD.
     */
//  Keep sorted. 
private static final String[] EMPTY_ELEMENTS = { "area", "base", "br", "col", "hr", "img", "input", "link", "meta", "p", "param" };
private JSPWikiMarkupParser m_cleanTranslator;
/** Holds the image URL for the duration of this parser */
private String m_outlinkImageURL = null;
/** Controls whether italic is restarted after a paragraph shift */
private boolean m_restartitalic = false;
private boolean m_restartbold = false;
private boolean m_newLine;
/** The token is a plain character. */
protected static final int CHARACTER = 0;
/** The token is a wikimarkup element. */
protected static final int ELEMENT = 1;
/** The token is to be ignored. */
protected static final int IGNORE = 2;
}
