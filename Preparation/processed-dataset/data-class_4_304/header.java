void method0() { 
//  
// Constants  
//  
// scanner states  
/** Scanner state: start of markup. */
protected static final int SCANNER_STATE_START_OF_MARKUP = 1;
/** Scanner state: comment. */
protected static final int SCANNER_STATE_COMMENT = 2;
/** Scanner state: processing instruction. */
protected static final int SCANNER_STATE_PI = 3;
/** Scanner state: DOCTYPE. */
protected static final int SCANNER_STATE_DOCTYPE = 4;
/** Scanner state: root element. */
protected static final int SCANNER_STATE_ROOT_ELEMENT = 6;
/** Scanner state: content. */
protected static final int SCANNER_STATE_CONTENT = 7;
/** Scanner state: reference. */
protected static final int SCANNER_STATE_REFERENCE = 8;
/** Scanner state: end of input. */
protected static final int SCANNER_STATE_END_OF_INPUT = 13;
/** Scanner state: terminated. */
protected static final int SCANNER_STATE_TERMINATED = 14;
/** Scanner state: CDATA section. */
protected static final int SCANNER_STATE_CDATA = 15;
/** Scanner state: Text declaration. */
protected static final int SCANNER_STATE_TEXT_DECL = 16;
// feature identifiers  
/** Feature identifier: namespaces. */
protected static final String NAMESPACES = Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE;
/** Feature identifier: notify built-in refereces. */
protected static final String NOTIFY_BUILTIN_REFS = Constants.XERCES_FEATURE_PREFIX + Constants.NOTIFY_BUILTIN_REFS_FEATURE;
// property identifiers  
/** Property identifier: entity resolver. */
protected static final String ENTITY_RESOLVER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_RESOLVER_PROPERTY;
// recognized features and properties  
/** Recognized features. */
private static final String[] RECOGNIZED_FEATURES = { NAMESPACES, VALIDATION, NOTIFY_BUILTIN_REFS, NOTIFY_CHAR_REFS };
/** Feature defaults. */
private static final Boolean[] FEATURE_DEFAULTS = { null, null, Boolean.FALSE, Boolean.FALSE };
/** Recognized properties. */
private static final String[] RECOGNIZED_PROPERTIES = { SYMBOL_TABLE, ERROR_REPORTER, ENTITY_MANAGER, ENTITY_RESOLVER };
/** Property defaults. */
private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null };
// debugging  
/** Debug scanner state. */
private static final boolean DEBUG_SCANNER_STATE = false;
/** Debug dispatcher. */
private static final boolean DEBUG_DISPATCHER = false;
/** Debug content dispatcher scanning. */
protected static final boolean DEBUG_CONTENT_SCANNING = false;
//  
// Data  
//  
// protected data  
/** Document handler. */
protected XMLDocumentHandler fDocumentHandler;
/** Entity stack. */
protected int[] fEntityStack = new int[4];
/** Markup depth. */
protected int fMarkupDepth;
/** Scanner state. */
protected int fScannerState;
/** SubScanner state: inside scanContent method. */
protected boolean fInScanContent = false;
/** has external dtd */
protected boolean fHasExternalDTD;
/** Standalone. */
protected boolean fStandalone;
/** True if [Entity Declared] is a VC; false if it is a WFC. */
protected boolean fIsEntityDeclaredVC;
/** External subset resolver. **/
protected ExternalSubsetResolver fExternalSubsetResolver;
// element information  
/** Current element. */
protected QName fCurrentElement;
/** Element stack. */
protected final ElementStack fElementStack = new ElementStack();
// other info  
/** Document system identifier. 
     * REVISIT:  So what's this used for?  - NG
    * protected String fDocumentSystemId;
     ******/
// features  
/** Notify built-in references. */
protected boolean fNotifyBuiltInRefs = false;
// dispatchers  
/** Active dispatcher. */
protected Dispatcher fDispatcher;
/** Content dispatcher. */
protected final Dispatcher fContentDispatcher = createContentDispatcher();
// temporary variables  
/** Element QName. */
protected final QName fElementQName = new QName();
/** Attribute QName. */
protected final QName fAttributeQName = new QName();
/** Element attributes. */
protected final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
/** String. */
protected final XMLString fTempString = new XMLString();
/** String. */
protected final XMLString fTempString2 = new XMLString();
/** Array of 3 strings. */
private final String[] fStrings = new String[3];
/** String buffer. */
private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
/** String buffer. */
private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
/** Another QName. */
private final QName fQName = new QName();
/** Single character array. */
private final char[] fSingleChar = new char[1];
/** 
     * Saw spaces after element name or between attributes.
     * 
     * This is reserved for the case where scanning of a start element spans
     * several methods, as is the case when scanning the start of a root element 
     * where a DTD external subset may be read after scanning the element name.
     */
private boolean fSawSpace;
/** Reusable Augmentations. */
private Augmentations fTempAugmentations = null;
}
