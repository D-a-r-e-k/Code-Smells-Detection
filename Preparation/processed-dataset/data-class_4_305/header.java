void method0() { 
//  
// Constants  
//  
// feature identifiers  
/** Feature identifier: validation. */
protected static final String VALIDATION = Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE;
/** Feature identifier: namespaces. */
protected static final String NAMESPACES = Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE;
/** Feature identifier: notify character references. */
protected static final String NOTIFY_CHAR_REFS = Constants.XERCES_FEATURE_PREFIX + Constants.NOTIFY_CHAR_REFS_FEATURE;
protected static final String PARSER_SETTINGS = Constants.XERCES_FEATURE_PREFIX + Constants.PARSER_SETTINGS;
// property identifiers  
/** Property identifier: symbol table. */
protected static final String SYMBOL_TABLE = Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;
/** Property identifier: error reporter. */
protected static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;
/** Property identifier: entity manager. */
protected static final String ENTITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY;
// debugging  
/** Debug attribute normalization. */
protected static final boolean DEBUG_ATTR_NORMALIZATION = false;
//  
// Data  
//  
// features  
/** 
     * Validation. This feature identifier is:
     * http://xml.org/sax/features/validation
     */
protected boolean fValidation = false;
/** Namespaces. */
protected boolean fNamespaces;
/** Character references notification. */
protected boolean fNotifyCharRefs = false;
/** Internal parser-settings feature */
protected boolean fParserSettings = true;
// properties  
/** Symbol table. */
protected SymbolTable fSymbolTable;
/** Error reporter. */
protected XMLErrorReporter fErrorReporter;
/** Entity manager. */
protected XMLEntityManager fEntityManager;
// protected data  
/** Entity scanner. */
protected XMLEntityScanner fEntityScanner;
/** Entity depth. */
protected int fEntityDepth;
/** Literal value of the last character refence scanned. */
protected String fCharRefLiteral = null;
/** Scanning attribute. */
protected boolean fScanningAttribute;
/** Report entity boundary. */
protected boolean fReportEntity;
// symbols  
/** Symbol: "version". */
protected static final String fVersionSymbol = "version".intern();
/** Symbol: "encoding". */
protected static final String fEncodingSymbol = "encoding".intern();
/** Symbol: "standalone". */
protected static final String fStandaloneSymbol = "standalone".intern();
/** Symbol: "amp". */
protected static final String fAmpSymbol = "amp".intern();
/** Symbol: "lt". */
protected static final String fLtSymbol = "lt".intern();
/** Symbol: "gt". */
protected static final String fGtSymbol = "gt".intern();
/** Symbol: "quot". */
protected static final String fQuotSymbol = "quot".intern();
/** Symbol: "apos". */
protected static final String fAposSymbol = "apos".intern();
// temporary variables  
// NOTE: These objects are private to help prevent accidental modification  
//       of values by a subclass. If there were protected *and* the sub-  
//       modified the values, it would be difficult to track down the real  
//       cause of the bug. By making these private, we avoid this   
//       possibility.  
/** String. */
private final XMLString fString = new XMLString();
/** String buffer. */
private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();
/** String buffer. */
private final XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
/** String buffer. */
private final XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
// temporary location for Resource identification information.  
protected final XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();
}
