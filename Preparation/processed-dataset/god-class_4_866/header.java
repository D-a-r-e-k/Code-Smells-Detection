void method0() { 
// 
// Constants 
// 
// doctype info: HTML 4.01 strict 
/** HTML 4.01 strict public identifier ("-//W3C//DTD HTML 4.01//EN"). */
public static final String HTML_4_01_STRICT_PUBID = "-//W3C//DTD HTML 4.01//EN";
/** HTML 4.01 strict system identifier ("http://www.w3.org/TR/html4/strict.dtd"). */
public static final String HTML_4_01_STRICT_SYSID = "http://www.w3.org/TR/html4/strict.dtd";
// doctype info: HTML 4.01 loose 
/** HTML 4.01 transitional public identifier ("-//W3C//DTD HTML 4.01 Transitional//EN"). */
public static final String HTML_4_01_TRANSITIONAL_PUBID = "-//W3C//DTD HTML 4.01 Transitional//EN";
/** HTML 4.01 transitional system identifier ("http://www.w3.org/TR/html4/loose.dtd"). */
public static final String HTML_4_01_TRANSITIONAL_SYSID = "http://www.w3.org/TR/html4/loose.dtd";
// doctype info: HTML 4.01 frameset 
/** HTML 4.01 frameset public identifier ("-//W3C//DTD HTML 4.01 Frameset//EN"). */
public static final String HTML_4_01_FRAMESET_PUBID = "-//W3C//DTD HTML 4.01 Frameset//EN";
/** HTML 4.01 frameset system identifier ("http://www.w3.org/TR/html4/frameset.dtd"). */
public static final String HTML_4_01_FRAMESET_SYSID = "http://www.w3.org/TR/html4/frameset.dtd";
// features 
/** Include infoset augmentations. */
protected static final String AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
/** Report errors. */
protected static final String REPORT_ERRORS = "http://cyberneko.org/html/features/report-errors";
/** Notify character entity references (e.g. &amp;#32;, &amp;#x20;, etc). */
public static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
/** 
     * Notify handler of built-in entity references (e.g. &amp;amp;, 
     * &amp;lt;, etc).
     * <p>
     * <strong>Note:</strong>
     * This only applies to the five pre-defined XML general entities.
     * Specifically, "amp", "lt", "gt", "quot", and "apos". This is done 
     * for compatibility with the Xerces feature.
     * <p>
     * To be notified of the built-in entity references in HTML, set the 
     * <code>http://cyberneko.org/html/features/scanner/notify-builtin-refs</code> 
     * feature to <code>true</code>.
     */
public static final String NOTIFY_XML_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
/** 
     * Notify handler of built-in entity references (e.g. &amp;nobr;, 
     * &amp;copy;, etc).
     * <p>
     * <strong>Note:</strong>
     * This <em>includes</em> the five pre-defined XML general entities.
     */
public static final String NOTIFY_HTML_BUILTIN_REFS = "http://cyberneko.org/html/features/scanner/notify-builtin-refs";
/** Fix Microsoft Windows&reg; character entity references. */
public static final String FIX_MSWINDOWS_REFS = "http://cyberneko.org/html/features/scanner/fix-mswindows-refs";
/** 
     * Strip HTML comment delimiters ("&lt;!&minus;&minus;" and 
     * "&minus;&minus;&gt;") from SCRIPT tag contents.
     */
public static final String SCRIPT_STRIP_COMMENT_DELIMS = "http://cyberneko.org/html/features/scanner/script/strip-comment-delims";
/** 
     * Strip XHTML CDATA delimiters ("&lt;![CDATA[" and "]]&gt;") from 
     * SCRIPT tag contents.
     */
public static final String SCRIPT_STRIP_CDATA_DELIMS = "http://cyberneko.org/html/features/scanner/script/strip-cdata-delims";
/** 
     * Strip HTML comment delimiters ("&lt;!&minus;&minus;" and 
     * "&minus;&minus;&gt;") from STYLE tag contents.
     */
public static final String STYLE_STRIP_COMMENT_DELIMS = "http://cyberneko.org/html/features/scanner/style/strip-comment-delims";
/** 
     * Strip XHTML CDATA delimiters ("&lt;![CDATA[" and "]]&gt;") from 
     * STYLE tag contents.
     */
public static final String STYLE_STRIP_CDATA_DELIMS = "http://cyberneko.org/html/features/scanner/style/strip-cdata-delims";
/**
     * Ignore specified charset found in the &lt;meta equiv='Content-Type'
     * content='text/html;charset=&hellip;'&gt; tag or in the &lt;?xml &hellip; encoding='&hellip;'&gt; processing instruction
     */
public static final String IGNORE_SPECIFIED_CHARSET = "http://cyberneko.org/html/features/scanner/ignore-specified-charset";
/** Scan CDATA sections. */
public static final String CDATA_SECTIONS = "http://cyberneko.org/html/features/scanner/cdata-sections";
/** Override doctype declaration public and system identifiers. */
public static final String OVERRIDE_DOCTYPE = "http://cyberneko.org/html/features/override-doctype";
/** Insert document type declaration. */
public static final String INSERT_DOCTYPE = "http://cyberneko.org/html/features/insert-doctype";
/** Parse &lt;noscript&gt;...&lt;/noscript&gt; content */
public static final String PARSE_NOSCRIPT_CONTENT = "http://cyberneko.org/html/features/parse-noscript-content";
/** Normalize attribute values. */
protected static final String NORMALIZE_ATTRIBUTES = "http://cyberneko.org/html/features/scanner/normalize-attrs";
/** Recognized features. */
private static final String[] RECOGNIZED_FEATURES = { AUGMENTATIONS, REPORT_ERRORS, NOTIFY_CHAR_REFS, NOTIFY_XML_BUILTIN_REFS, NOTIFY_HTML_BUILTIN_REFS, FIX_MSWINDOWS_REFS, SCRIPT_STRIP_CDATA_DELIMS, SCRIPT_STRIP_COMMENT_DELIMS, STYLE_STRIP_CDATA_DELIMS, STYLE_STRIP_COMMENT_DELIMS, IGNORE_SPECIFIED_CHARSET, CDATA_SECTIONS, OVERRIDE_DOCTYPE, INSERT_DOCTYPE, NORMALIZE_ATTRIBUTES, PARSE_NOSCRIPT_CONTENT };
/** Recognized features defaults. */
private static final Boolean[] RECOGNIZED_FEATURES_DEFAULTS = { null, null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE };
// properties 
/** Modify HTML element names: { "upper", "lower", "default" }. */
protected static final String NAMES_ELEMS = "http://cyberneko.org/html/properties/names/elems";
/** Modify HTML attribute names: { "upper", "lower", "default" }. */
protected static final String NAMES_ATTRS = "http://cyberneko.org/html/properties/names/attrs";
/** Default encoding. */
protected static final String DEFAULT_ENCODING = "http://cyberneko.org/html/properties/default-encoding";
/** Error reporter. */
protected static final String ERROR_REPORTER = "http://cyberneko.org/html/properties/error-reporter";
/** Doctype declaration public identifier. */
protected static final String DOCTYPE_PUBID = "http://cyberneko.org/html/properties/doctype/pubid";
/** Doctype declaration system identifier. */
protected static final String DOCTYPE_SYSID = "http://cyberneko.org/html/properties/doctype/sysid";
/** Recognized properties. */
private static final String[] RECOGNIZED_PROPERTIES = { NAMES_ELEMS, NAMES_ATTRS, DEFAULT_ENCODING, ERROR_REPORTER, DOCTYPE_PUBID, DOCTYPE_SYSID };
/** Recognized properties defaults. */
private static final Object[] RECOGNIZED_PROPERTIES_DEFAULTS = { null, null, "Windows-1252", null, HTML_4_01_TRANSITIONAL_PUBID, HTML_4_01_TRANSITIONAL_SYSID };
// states 
/** State: content. */
protected static final short STATE_CONTENT = 0;
/** State: markup bracket. */
protected static final short STATE_MARKUP_BRACKET = 1;
/** State: start document. */
protected static final short STATE_START_DOCUMENT = 10;
/** State: end document. */
protected static final short STATE_END_DOCUMENT = 11;
// modify HTML names 
/** Don't modify HTML names. */
protected static final short NAMES_NO_CHANGE = 0;
/** Uppercase HTML names. */
protected static final short NAMES_UPPERCASE = 1;
/** Lowercase HTML names. */
protected static final short NAMES_LOWERCASE = 2;
// defaults 
/** Default buffer size. */
protected static final int DEFAULT_BUFFER_SIZE = 2048;
// debugging 
/** Set to true to debug changes in the scanner. */
private static final boolean DEBUG_SCANNER = false;
/** Set to true to debug changes in the scanner state. */
private static final boolean DEBUG_SCANNER_STATE = false;
/** Set to true to debug the buffer. */
private static final boolean DEBUG_BUFFER = false;
/** Set to true to debug character encoding handling. */
private static final boolean DEBUG_CHARSET = false;
/** Set to true to debug callbacks. */
protected static final boolean DEBUG_CALLBACKS = false;
// static vars 
/** Synthesized event info item. */
protected static final HTMLEventInfo SYNTHESIZED_ITEM = new HTMLEventInfo.SynthesizedItem();
private static final BitSet ENTITY_CHARS = new BitSet();
// 
// Data 
// 
// features 
/** Augmentations. */
protected boolean fAugmentations;
/** Report errors. */
protected boolean fReportErrors;
/** Notify character entity references. */
protected boolean fNotifyCharRefs;
/** Notify XML built-in general entity references. */
protected boolean fNotifyXmlBuiltinRefs;
/** Notify HTML built-in general entity references. */
protected boolean fNotifyHtmlBuiltinRefs;
/** Fix Microsoft Windows&reg; character entity references. */
protected boolean fFixWindowsCharRefs;
/** Strip CDATA delimiters from SCRIPT tags. */
protected boolean fScriptStripCDATADelims;
/** Strip comment delimiters from SCRIPT tags. */
protected boolean fScriptStripCommentDelims;
/** Strip CDATA delimiters from STYLE tags. */
protected boolean fStyleStripCDATADelims;
/** Strip comment delimiters from STYLE tags. */
protected boolean fStyleStripCommentDelims;
/** Ignore specified character set. */
protected boolean fIgnoreSpecifiedCharset;
/** CDATA sections. */
protected boolean fCDATASections;
/** Override doctype declaration public and system identifiers. */
protected boolean fOverrideDoctype;
/** Insert document type declaration. */
protected boolean fInsertDoctype;
/** Normalize attribute values. */
protected boolean fNormalizeAttributes;
/** Parse noscript content. */
protected boolean fParseNoScriptContent;
/** Parse noframes content. */
protected boolean fParseNoFramesContent;
// properties 
/** Modify HTML element names. */
protected short fNamesElems;
/** Modify HTML attribute names. */
protected short fNamesAttrs;
/** Default encoding. */
protected String fDefaultIANAEncoding;
/** Error reporter. */
protected HTMLErrorReporter fErrorReporter;
/** Doctype declaration public identifier. */
protected String fDoctypePubid;
/** Doctype declaration system identifier. */
protected String fDoctypeSysid;
// boundary locator information 
/** Beginning line number. */
protected int fBeginLineNumber;
/** Beginning column number. */
protected int fBeginColumnNumber;
/** Beginning character offset in the file. */
protected int fBeginCharacterOffset;
/** Ending line number. */
protected int fEndLineNumber;
/** Ending column number. */
protected int fEndColumnNumber;
/** Ending character offset in the file. */
protected int fEndCharacterOffset;
// state 
/** The playback byte stream. */
protected PlaybackInputStream fByteStream;
/** Current entity. */
protected CurrentEntity fCurrentEntity;
/** The current entity stack. */
protected final Stack fCurrentEntityStack = new Stack();
/** The current scanner. */
protected Scanner fScanner;
/** The current scanner state. */
protected short fScannerState;
/** The document handler. */
protected XMLDocumentHandler fDocumentHandler;
/** Auto-detected IANA encoding. */
protected String fIANAEncoding;
/** Auto-detected Java encoding. */
protected String fJavaEncoding;
/** True if the encoding matches "ISO-8859-*". */
protected boolean fIso8859Encoding;
/** Element count. */
protected int fElementCount;
/** Element depth. */
protected int fElementDepth;
// scanners 
/** Content scanner. */
protected Scanner fContentScanner = new ContentScanner();
/** 
     * Special scanner used for elements whose content needs to be scanned 
     * as plain text, ignoring markup such as elements and entity references.
     * For example: &lt;SCRIPT&gt; and &lt;COMMENT&gt;.
     */
protected SpecialScanner fSpecialScanner = new SpecialScanner();
// temp vars 
/** String buffer. */
protected final XMLStringBuffer fStringBuffer = new XMLStringBuffer(1024);
/** String buffer. */
private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer(1024);
/** Non-normalized attribute string buffer. */
private final XMLStringBuffer fNonNormAttr = new XMLStringBuffer(128);
/** Augmentations. */
private final HTMLAugmentations fInfosetAugs = new HTMLAugmentations();
/** Location infoset item. */
private final LocationItem fLocationItem = new LocationItem();
/** Single boolean array. */
private final boolean[] fSingleBoolean = { false };
/** Resource identifier. */
private final XMLResourceIdentifierImpl fResourceId = new XMLResourceIdentifierImpl();
}
