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
/** Simple report format. */
protected static final String SIMPLE_ERROR_FORMAT = "http://cyberneko.org/html/features/report-errors/simple";
/** Balance tags. */
protected static final String BALANCE_TAGS = "http://cyberneko.org/html/features/balance-tags";
// properties 
/** Modify HTML element names: { "upper", "lower", "default" }. */
protected static final String NAMES_ELEMS = "http://cyberneko.org/html/properties/names/elems";
/** Modify HTML attribute names: { "upper", "lower", "default" }. */
protected static final String NAMES_ATTRS = "http://cyberneko.org/html/properties/names/attrs";
/** Pipeline filters. */
protected static final String FILTERS = "http://cyberneko.org/html/properties/filters";
/** Error reporter. */
protected static final String ERROR_REPORTER = "http://cyberneko.org/html/properties/error-reporter";
// other 
/** Error domain. */
protected static final String ERROR_DOMAIN = "http://cyberneko.org/html";
// private 
/** Document source class array. */
private static final Class[] DOCSOURCE = { XMLDocumentSource.class };
// 
// Data 
// 
// handlers 
/** Document handler. */
protected XMLDocumentHandler fDocumentHandler;
/** DTD handler. */
protected XMLDTDHandler fDTDHandler;
/** DTD content model handler. */
protected XMLDTDContentModelHandler fDTDContentModelHandler;
/** Error handler. */
protected XMLErrorHandler fErrorHandler = new DefaultErrorHandler();
// other settings 
/** Entity resolver. */
protected XMLEntityResolver fEntityResolver;
/** Locale. */
protected Locale fLocale = Locale.getDefault();
// state 
/** 
     * Stream opened by parser. Therefore, must close stream manually upon
     * termination of parsing.
     */
protected boolean fCloseStream;
// components 
/** Components. */
protected final Vector fHTMLComponents = new Vector(2);
// pipeline 
/** Document scanner. */
protected final HTMLScanner fDocumentScanner = createDocumentScanner();
/** HTML tag balancer. */
protected final HTMLTagBalancer fTagBalancer = new HTMLTagBalancer();
/** Namespace binder. */
protected final NamespaceBinder fNamespaceBinder = new NamespaceBinder();
// other components 
/** Error reporter. */
protected final HTMLErrorReporter fErrorReporter = new ErrorReporter();
// HACK: workarounds Xerces 2.0.x problems 
/** Parser version is Xerces 2.0.0. */
protected static boolean XERCES_2_0_0 = false;
/** Parser version is Xerces 2.0.1. */
protected static boolean XERCES_2_0_1 = false;
/** Parser version is XML4J 4.0.x. */
protected static boolean XML4J_4_0_x = false;
}
