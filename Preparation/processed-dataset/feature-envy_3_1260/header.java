void method0() { 
// 
// Constants 
// 
// features 
/** Namespaces. */
protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/** Include infoset augmentations. */
protected static final String AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
/** Report errors. */
protected static final String REPORT_ERRORS = "http://cyberneko.org/html/features/report-errors";
/** Document fragment balancing only (deprecated). */
protected static final String DOCUMENT_FRAGMENT_DEPRECATED = "http://cyberneko.org/html/features/document-fragment";
/** Document fragment balancing only. */
protected static final String DOCUMENT_FRAGMENT = "http://cyberneko.org/html/features/balance-tags/document-fragment";
/** Ignore outside content. */
protected static final String IGNORE_OUTSIDE_CONTENT = "http://cyberneko.org/html/features/balance-tags/ignore-outside-content";
/** Recognized features. */
private static final String[] RECOGNIZED_FEATURES = { NAMESPACES, AUGMENTATIONS, REPORT_ERRORS, DOCUMENT_FRAGMENT_DEPRECATED, DOCUMENT_FRAGMENT, IGNORE_OUTSIDE_CONTENT };
/** Recognized features defaults. */
private static final Boolean[] RECOGNIZED_FEATURES_DEFAULTS = { null, null, null, null, Boolean.FALSE, Boolean.FALSE };
// properties 
/** Modify HTML element names: { "upper", "lower", "default" }. */
protected static final String NAMES_ELEMS = "http://cyberneko.org/html/properties/names/elems";
/** Modify HTML attribute names: { "upper", "lower", "default" }. */
protected static final String NAMES_ATTRS = "http://cyberneko.org/html/properties/names/attrs";
/** Error reporter. */
protected static final String ERROR_REPORTER = "http://cyberneko.org/html/properties/error-reporter";
/**
     * <font color="red">EXPERIMENTAL: may change in next release</font><br/>
     * Name of the property holding the stack of elements in which context a document fragment should be parsed.
     **/
public static final String FRAGMENT_CONTEXT_STACK = "http://cyberneko.org/html/properties/balance-tags/fragment-context-stack";
/** Recognized properties. */
private static final String[] RECOGNIZED_PROPERTIES = { NAMES_ELEMS, NAMES_ATTRS, ERROR_REPORTER, FRAGMENT_CONTEXT_STACK };
/** Recognized properties defaults. */
private static final Object[] RECOGNIZED_PROPERTIES_DEFAULTS = { null, null, null, null };
// modify HTML names 
/** Don't modify HTML names. */
protected static final short NAMES_NO_CHANGE = 0;
/** Match HTML element names. */
protected static final short NAMES_MATCH = 0;
/** Uppercase HTML names. */
protected static final short NAMES_UPPERCASE = 1;
/** Lowercase HTML names. */
protected static final short NAMES_LOWERCASE = 2;
// static vars 
/** Synthesized event info item. */
protected static final HTMLEventInfo SYNTHESIZED_ITEM = new HTMLEventInfo.SynthesizedItem();
// 
// Data 
// 
// features 
/** Namespaces. */
protected boolean fNamespaces;
/** Include infoset augmentations. */
protected boolean fAugmentations;
/** Report errors. */
protected boolean fReportErrors;
/** Document fragment balancing only. */
protected boolean fDocumentFragment;
/** Ignore outside content. */
protected boolean fIgnoreOutsideContent;
// properties 
/** Modify HTML element names. */
protected short fNamesElems;
/** Modify HTML attribute names. */
protected short fNamesAttrs;
/** Error reporter. */
protected HTMLErrorReporter fErrorReporter;
// connections 
/** The document source. */
protected XMLDocumentSource fDocumentSource;
/** The document handler. */
protected XMLDocumentHandler fDocumentHandler;
// state 
/** The element stack. */
protected final InfoStack fElementStack = new InfoStack();
/** The inline stack. */
protected final InfoStack fInlineStack = new InfoStack();
/** True if seen anything. Important for xml declaration. */
protected boolean fSeenAnything;
/** True if root element has been seen. */
protected boolean fSeenDoctype;
/** True if root element has been seen. */
protected boolean fSeenRootElement;
/** 
     * True if seen the end of the document element. In other words, 
     * this variable is set to false <em>until</em> the end &lt;/HTML&gt; 
     * tag is seen (or synthesized). This is used to ensure that 
     * extraneous events after the end of the document element do not 
     * make the document stream ill-formed.
     */
protected boolean fSeenRootElementEnd;
/** True if seen &lt;head&lt; element. */
protected boolean fSeenHeadElement;
/** True if seen &lt;body&lt; element. */
protected boolean fSeenBodyElement;
/** True if a form is in the stack (allow to discard opening of nested forms) */
protected boolean fOpenedForm;
// temp vars 
/** A qualified name. */
private final QName fQName = new QName();
/** Empty attributes. */
private final XMLAttributes fEmptyAttrs = new XMLAttributesImpl();
/** Augmentations. */
private final HTMLAugmentations fInfosetAugs = new HTMLAugmentations();
protected HTMLTagBalancingListener tagBalancingListener;
private LostText lostText_ = new LostText();
private boolean forcedStartElement_ = false;
private boolean forcedEndElement_ = false;
/**
     * Stack of elements determining the context in which a document fragment should be parsed
     */
private QName[] fragmentContextStack_ = null;
private int fragmentContextStackSize_ = 0;
// not 0 only when a fragment is parsed and fragmentContextStack_ is set 
private List /*ElementEntry*/
endElementsBuffer_ = new ArrayList();
}
