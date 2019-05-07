void method0() { 
//  
// Constants  
//  
/** Symbol: "&lt;&lt;datatypes>>". */
/** Top level scope (-1). */
private static final int TOP_LEVEL_SCOPE = -1;
// feature identifiers  
/** Feature identifier: namespaces. */
protected static final String NAMESPACES = Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE;
/** Feature identifier: validation. */
protected static final String VALIDATION = Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE;
/** Feature identifier: dynamic validation. */
protected static final String DYNAMIC_VALIDATION = Constants.XERCES_FEATURE_PREFIX + Constants.DYNAMIC_VALIDATION_FEATURE;
/** Feature identifier: balance syntax trees. */
protected static final String BALANCE_SYNTAX_TREES = Constants.XERCES_FEATURE_PREFIX + Constants.BALANCE_SYNTAX_TREES;
/** Feature identifier: warn on duplicate attdef */
protected static final String WARN_ON_DUPLICATE_ATTDEF = Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_DUPLICATE_ATTDEF_FEATURE;
protected static final String PARSER_SETTINGS = Constants.XERCES_FEATURE_PREFIX + Constants.PARSER_SETTINGS;
// property identifiers  
/** Property identifier: symbol table. */
protected static final String SYMBOL_TABLE = Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;
/** Property identifier: error reporter. */
protected static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;
/** Property identifier: grammar pool. */
protected static final String GRAMMAR_POOL = Constants.XERCES_PROPERTY_PREFIX + Constants.XMLGRAMMAR_POOL_PROPERTY;
/** Property identifier: datatype validator factory. */
protected static final String DATATYPE_VALIDATOR_FACTORY = Constants.XERCES_PROPERTY_PREFIX + Constants.DATATYPE_VALIDATOR_FACTORY_PROPERTY;
// property identifier:  ValidationManager  
protected static final String VALIDATION_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.VALIDATION_MANAGER_PROPERTY;
// recognized features and properties  
/** Recognized features. */
private static final String[] RECOGNIZED_FEATURES = { NAMESPACES, VALIDATION, DYNAMIC_VALIDATION, BALANCE_SYNTAX_TREES };
/** Feature defaults. */
private static final Boolean[] FEATURE_DEFAULTS = { null, null, Boolean.FALSE, Boolean.FALSE };
/** Recognized properties. */
private static final String[] RECOGNIZED_PROPERTIES = { SYMBOL_TABLE, ERROR_REPORTER, GRAMMAR_POOL, DATATYPE_VALIDATOR_FACTORY, VALIDATION_MANAGER };
/** Property defaults. */
private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, null };
// debugging  
/** Compile to true to debug attributes. */
private static final boolean DEBUG_ATTRIBUTES = false;
/** Compile to true to debug element children. */
private static final boolean DEBUG_ELEMENT_CHILDREN = false;
//          
// Data  
//  
// updated during reset  
protected ValidationManager fValidationManager = null;
// validation state  
protected final ValidationState fValidationState = new ValidationState();
// features  
/** Namespaces. */
protected boolean fNamespaces;
/** Validation. */
protected boolean fValidation;
/** Validation against only DTD */
protected boolean fDTDValidation;
/** 
     * Dynamic validation. This state of this feature is only useful when
     * the validation feature is set to <code>true</code>.
     */
protected boolean fDynamicValidation;
/** Controls whether the DTD grammar produces balanced syntax trees. */
protected boolean fBalanceSyntaxTrees;
/** warn on duplicate attribute definition, this feature works only when validation is true */
protected boolean fWarnDuplicateAttdef;
// properties  
/** Symbol table. */
protected SymbolTable fSymbolTable;
/** Error reporter. */
protected XMLErrorReporter fErrorReporter;
// the grammar pool  
protected XMLGrammarPool fGrammarPool;
/** Grammar bucket. */
protected DTDGrammarBucket fGrammarBucket;
/* location of the document as passed in from startDocument call */
protected XMLLocator fDocLocation;
/** Namespace support. */
protected NamespaceContext fNamespaceContext = null;
/** Datatype validator factory. */
protected DTDDVFactory fDatatypeValidatorFactory;
// handlers  
/** Document handler. */
protected XMLDocumentHandler fDocumentHandler;
protected XMLDocumentSource fDocumentSource;
// grammars  
/** DTD Grammar. */
protected DTDGrammar fDTDGrammar;
// state  
/** True if seen DOCTYPE declaration. */
protected boolean fSeenDoctypeDecl = false;
/** Perform validation. */
private boolean fPerformValidation;
/** Schema type: None, DTD, Schema */
private String fSchemaType;
// information regarding the current element  
/** Current element name. */
private final QName fCurrentElement = new QName();
/** Current element index. */
private int fCurrentElementIndex = -1;
/** Current content spec type. */
private int fCurrentContentSpecType = -1;
/** The root element name. */
private final QName fRootElement = new QName();
private boolean fInCDATASection = false;
// element stack  
/** Element index stack. */
private int[] fElementIndexStack = new int[8];
/** Content spec type stack. */
private int[] fContentSpecTypeStack = new int[8];
/** Element name stack. */
private QName[] fElementQNamePartsStack = new QName[8];
// children list and offset stack  
/** 
     * Element children. This data structure is a growing stack that
     * holds the children of elements from the root to the current
     * element depth. This structure never gets "deeper" than the
     * deepest element. Space is re-used once each element is closed.
     * <p>
     * <strong>Note:</strong> This is much more efficient use of memory
     * than creating new arrays for each element depth.
     * <p>
     * <strong>Note:</strong> The use of this data structure is for
     * validation "on the way out". If the validation model changes to
     * "on the way in", then this data structure is not needed.
     */
private QName[] fElementChildren = new QName[32];
/** Element children count. */
private int fElementChildrenLength = 0;
/** 
     * Element children offset stack. This stack refers to offsets
     * into the <code>fElementChildren</code> array.
     * @see #fElementChildren
     */
private int[] fElementChildrenOffsetStack = new int[32];
/** Element depth. */
private int fElementDepth = -1;
// validation states  
/** True if seen the root element. */
private boolean fSeenRootElement = false;
/** True if inside of element content. */
private boolean fInElementContent = false;
// temporary variables  
/** Temporary element declaration. */
private XMLElementDecl fTempElementDecl = new XMLElementDecl();
/** Temporary atribute declaration. */
private final XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
/** Temporary entity declaration. */
private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
/** Temporary qualified name. */
private final QName fTempQName = new QName();
/** Temporary string buffers. */
private final StringBuffer fBuffer = new StringBuffer();
// symbols: general  
// attribute validators  
/** Datatype validator: ID. */
protected DatatypeValidator fValID;
/** Datatype validator: IDREF. */
protected DatatypeValidator fValIDRef;
/** Datatype validator: IDREFS. */
protected DatatypeValidator fValIDRefs;
/** Datatype validator: ENTITY. */
protected DatatypeValidator fValENTITY;
/** Datatype validator: ENTITIES. */
protected DatatypeValidator fValENTITIES;
/** Datatype validator: NMTOKEN. */
protected DatatypeValidator fValNMTOKEN;
/** Datatype validator: NMTOKENS. */
protected DatatypeValidator fValNMTOKENS;
/** Datatype validator: NOTATION. */
protected DatatypeValidator fValNOTATION;
}
