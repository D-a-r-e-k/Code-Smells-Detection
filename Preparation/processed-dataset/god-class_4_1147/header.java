void method0() { 
//  
// Constants  
//  
private static final boolean DEBUG = false;
// feature identifiers  
/** Feature identifier: validation. */
protected static final String VALIDATION = Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE;
/** Feature identifier: validation. */
protected static final String SCHEMA_VALIDATION = Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_VALIDATION_FEATURE;
/** Feature identifier: schema full checking*/
protected static final String SCHEMA_FULL_CHECKING = Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_FULL_CHECKING;
/** Feature identifier: dynamic validation. */
protected static final String DYNAMIC_VALIDATION = Constants.XERCES_FEATURE_PREFIX + Constants.DYNAMIC_VALIDATION_FEATURE;
/** Feature identifier: expose schema normalized value */
protected static final String NORMALIZE_DATA = Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_NORMALIZED_VALUE;
/** Feature identifier: send element default value via characters() */
protected static final String SCHEMA_ELEMENT_DEFAULT = Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_ELEMENT_DEFAULT;
/** Feature identifier: augment PSVI */
protected static final String SCHEMA_AUGMENT_PSVI = Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_AUGMENT_PSVI;
/** Feature identifier: whether to recognize java encoding names */
protected static final String ALLOW_JAVA_ENCODINGS = Constants.XERCES_FEATURE_PREFIX + Constants.ALLOW_JAVA_ENCODINGS_FEATURE;
/** Feature identifier: standard uri conformant feature. */
protected static final String STANDARD_URI_CONFORMANT_FEATURE = Constants.XERCES_FEATURE_PREFIX + Constants.STANDARD_URI_CONFORMANT_FEATURE;
/** Feature: generate synthetic annotations */
protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = Constants.XERCES_FEATURE_PREFIX + Constants.GENERATE_SYNTHETIC_ANNOTATIONS_FEATURE;
/** Feature identifier: validate annotations. */
protected static final String VALIDATE_ANNOTATIONS = Constants.XERCES_FEATURE_PREFIX + Constants.VALIDATE_ANNOTATIONS_FEATURE;
/** Feature identifier: honour all schemaLocations */
protected static final String HONOUR_ALL_SCHEMALOCATIONS = Constants.XERCES_FEATURE_PREFIX + Constants.HONOUR_ALL_SCHEMALOCATIONS_FEATURE;
/** Feature identifier: use grammar pool only */
protected static final String USE_GRAMMAR_POOL_ONLY = Constants.XERCES_FEATURE_PREFIX + Constants.USE_GRAMMAR_POOL_ONLY_FEATURE;
/** Feature identifier: whether to continue parsing a schema after a fatal error is encountered */
protected static final String CONTINUE_AFTER_FATAL_ERROR = Constants.XERCES_FEATURE_PREFIX + Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE;
protected static final String PARSER_SETTINGS = Constants.XERCES_FEATURE_PREFIX + Constants.PARSER_SETTINGS;
/** Feature identifier: namespace growth */
protected static final String NAMESPACE_GROWTH = Constants.XERCES_FEATURE_PREFIX + Constants.NAMESPACE_GROWTH_FEATURE;
/** Feature identifier: tolerate duplicates */
protected static final String TOLERATE_DUPLICATES = Constants.XERCES_FEATURE_PREFIX + Constants.TOLERATE_DUPLICATES_FEATURE;
/** Feature identifier: whether to ignore xsi:type attributes until a global element declaration is encountered */
protected static final String IGNORE_XSI_TYPE = Constants.XERCES_FEATURE_PREFIX + Constants.IGNORE_XSI_TYPE_FEATURE;
/** Feature identifier: whether to ignore ID/IDREF errors */
protected static final String ID_IDREF_CHECKING = Constants.XERCES_FEATURE_PREFIX + Constants.ID_IDREF_CHECKING_FEATURE;
/** Feature identifier: whether to ignore unparsed entity errors */
protected static final String UNPARSED_ENTITY_CHECKING = Constants.XERCES_FEATURE_PREFIX + Constants.UNPARSED_ENTITY_CHECKING_FEATURE;
/** Feature identifier: whether to ignore identity constraint errors */
protected static final String IDENTITY_CONSTRAINT_CHECKING = Constants.XERCES_FEATURE_PREFIX + Constants.IDC_CHECKING_FEATURE;
// property identifiers  
/** Property identifier: symbol table. */
public static final String SYMBOL_TABLE = Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;
/** Property identifier: error reporter. */
public static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;
/** Property identifier: entity resolver. */
public static final String ENTITY_RESOLVER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_RESOLVER_PROPERTY;
/** Property identifier: grammar pool. */
public static final String XMLGRAMMAR_POOL = Constants.XERCES_PROPERTY_PREFIX + Constants.XMLGRAMMAR_POOL_PROPERTY;
protected static final String VALIDATION_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.VALIDATION_MANAGER_PROPERTY;
protected static final String ENTITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY;
/** Property identifier: schema location. */
protected static final String SCHEMA_LOCATION = Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_LOCATION;
/** Property identifier: no namespace schema location. */
protected static final String SCHEMA_NONS_LOCATION = Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_NONS_LOCATION;
/** Property identifier: JAXP schema source. */
protected static final String JAXP_SCHEMA_SOURCE = Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_SOURCE;
/** Property identifier: JAXP schema language. */
protected static final String JAXP_SCHEMA_LANGUAGE = Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_LANGUAGE;
/** Property identifier: root type definition. */
protected static final String ROOT_TYPE_DEF = Constants.XERCES_PROPERTY_PREFIX + Constants.ROOT_TYPE_DEFINITION_PROPERTY;
/** Property identifier: root element declaration. */
protected static final String ROOT_ELEMENT_DECL = Constants.XERCES_PROPERTY_PREFIX + Constants.ROOT_ELEMENT_DECLARATION_PROPERTY;
/** Property identifier: Schema DV Factory */
protected static final String SCHEMA_DV_FACTORY = Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_DV_FACTORY_PROPERTY;
// recognized features and properties  
/** Recognized features. */
private static final String[] RECOGNIZED_FEATURES = { VALIDATION, SCHEMA_VALIDATION, DYNAMIC_VALIDATION, SCHEMA_FULL_CHECKING, ALLOW_JAVA_ENCODINGS, CONTINUE_AFTER_FATAL_ERROR, STANDARD_URI_CONFORMANT_FEATURE, GENERATE_SYNTHETIC_ANNOTATIONS, VALIDATE_ANNOTATIONS, HONOUR_ALL_SCHEMALOCATIONS, USE_GRAMMAR_POOL_ONLY, IGNORE_XSI_TYPE, ID_IDREF_CHECKING, IDENTITY_CONSTRAINT_CHECKING, UNPARSED_ENTITY_CHECKING, NAMESPACE_GROWTH, TOLERATE_DUPLICATES };
/** Feature defaults. */
private static final Boolean[] FEATURE_DEFAULTS = { null, // NOTE: The following defaults are nulled out on purpose.  
//       If they are set, then when the XML Schema validator  
//       is constructed dynamically, these values may override  
//       those set by the application. This goes against the  
//       whole purpose of XMLComponent#getFeatureDefault but  
//       it can't be helped in this case. -Ac  
// NOTE: Instead of adding default values here, add them (and  
//       the corresponding recognized features) to the objects  
//       that have an XMLSchemaValidator instance as a member,  
//       such as the parser configurations. -PM  
null, //Boolean.FALSE,  
null, //Boolean.FALSE,  
null, //Boolean.FALSE,  
null, //Boolean.FALSE,  
null, //Boolean.FALSE,  
null, null, null, null, null, null, null, null, null, null, null };
/** Recognized properties. */
private static final String[] RECOGNIZED_PROPERTIES = { SYMBOL_TABLE, ERROR_REPORTER, ENTITY_RESOLVER, VALIDATION_MANAGER, SCHEMA_LOCATION, SCHEMA_NONS_LOCATION, JAXP_SCHEMA_SOURCE, JAXP_SCHEMA_LANGUAGE, ROOT_TYPE_DEF, ROOT_ELEMENT_DECL, SCHEMA_DV_FACTORY };
/** Property defaults. */
private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, null, null, null, null, null, null, null };
// this is the number of valuestores of each kind  
// we expect an element to have.  It's almost  
// never > 1; so leave it at that.  
protected static final int ID_CONSTRAINT_NUM = 1;
// xsi:* attribute declarations  
static final XSAttributeDecl XSI_TYPE = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_TYPE);
static final XSAttributeDecl XSI_NIL = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NIL);
static final XSAttributeDecl XSI_SCHEMALOCATION = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_SCHEMALOCATION);
static final XSAttributeDecl XSI_NONAMESPACESCHEMALOCATION = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
//  
private static final Hashtable EMPTY_TABLE = new Hashtable();
//  
// Data  
//  
/** current PSVI element info */
protected ElementPSVImpl fCurrentPSVI = new ElementPSVImpl();
// since it is the responsibility of each component to an  
// Augmentations parameter if one is null, to save ourselves from  
// having to create this object continually, it is created here.  
// If it is not present in calls that we're passing on, we *must*  
// clear this before we introduce it into the pipeline.  
protected final AugmentationsImpl fAugmentations = new AugmentationsImpl();
// this is included for the convenience of handleEndElement  
protected XMLString fDefaultValue;
// Validation features  
protected boolean fDynamicValidation = false;
protected boolean fSchemaDynamicValidation = false;
protected boolean fDoValidation = false;
protected boolean fFullChecking = false;
protected boolean fNormalizeData = true;
protected boolean fSchemaElementDefault = true;
protected boolean fAugPSVI = true;
protected boolean fIdConstraint = false;
protected boolean fUseGrammarPoolOnly = false;
// Namespace growth feature  
protected boolean fNamespaceGrowth = false;
/** Schema type: None, DTD, Schema */
private String fSchemaType = null;
// to indicate whether we are in the scope of entity reference or CData  
protected boolean fEntityRef = false;
protected boolean fInCDATA = false;
// properties  
/** Symbol table. */
protected SymbolTable fSymbolTable;
/**
     * While parsing a document, keep the location of the document.
     */
private XMLLocator fLocator;
/** Error reporter. */
protected final XSIErrorReporter fXSIErrorReporter = new XSIErrorReporter();
/** Entity resolver */
protected XMLEntityResolver fEntityResolver;
// updated during reset  
protected ValidationManager fValidationManager = null;
protected ConfigurableValidationState fValidationState = new ConfigurableValidationState();
protected XMLGrammarPool fGrammarPool;
// schema location property values  
protected String fExternalSchemas = null;
protected String fExternalNoNamespaceSchema = null;
//JAXP Schema Source property  
protected Object fJaxpSchemaSource = null;
/** Schema Grammar Description passed,  to give a chance to application to supply the Grammar */
protected final XSDDescription fXSDDescription = new XSDDescription();
protected final Hashtable fLocationPairs = new Hashtable();
protected final Hashtable fExpandedLocationPairs = new Hashtable();
protected final ArrayList fUnparsedLocations = new ArrayList();
// handlers  
/** Document handler. */
protected XMLDocumentHandler fDocumentHandler;
protected XMLDocumentSource fDocumentSource;
// endEntity(String)  
// constants  
static final int INITIAL_STACK_SIZE = 8;
static final int INC_STACK_SIZE = 8;
//  
// Data  
//  
// Schema Normalization  
private static final boolean DEBUG_NORMALIZATION = false;
// temporary empty string buffer.  
private final XMLString fEmptyXMLStr = new XMLString(null, 0, -1);
// temporary character buffer, and empty string buffer.  
private static final int BUFFER_SIZE = 20;
private final XMLString fNormalizedStr = new XMLString();
private boolean fFirstChunk = true;
// got first chunk in characters() (SAX)  
private boolean fTrailing = false;
// Previous chunk had a trailing space  
private short fWhiteSpace = -1;
//whiteSpace: preserve/replace/collapse  
private boolean fUnionType = false;
/** Schema grammar resolver. */
private final XSGrammarBucket fGrammarBucket = new XSGrammarBucket();
private final SubstitutionGroupHandler fSubGroupHandler = new SubstitutionGroupHandler(fGrammarBucket);
/** the DV usd to convert xsi:type to a QName */
// REVISIT: in new simple type design, make things in DVs static,  
//          so that we can QNameDV.getCompiledForm()  
private final XSSimpleType fQNameDV = (XSSimpleType) SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(SchemaSymbols.ATTVAL_QNAME);
private final CMNodeFactory nodeFactory = new CMNodeFactory();
/** used to build content models */
// REVISIT: create decl pool, and pass it to each traversers  
private final CMBuilder fCMBuilder = new CMBuilder(nodeFactory);
// Schema grammar loader  
private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader(fXSIErrorReporter.fErrorReporter, fGrammarBucket, fSubGroupHandler, fCMBuilder);
// state  
/** String representation of the validation root. */
// REVISIT: what do we store here? QName, XPATH, some ID? use rawname now.  
private String fValidationRoot;
/** Skip validation: anything below this level should be skipped */
private int fSkipValidationDepth;
/** anything above this level has validation_attempted != full */
private int fNFullValidationDepth;
/** anything above this level has validation_attempted != none */
private int fNNoneValidationDepth;
/** Element depth: -2: validator not in pipeline; >= -1 current depth. */
private int fElementDepth;
/** Seen sub elements. */
private boolean fSubElement;
/** Seen sub elements stack. */
private boolean[] fSubElementStack = new boolean[INITIAL_STACK_SIZE];
/** Current element declaration. */
private XSElementDecl fCurrentElemDecl;
/** Element decl stack. */
private XSElementDecl[] fElemDeclStack = new XSElementDecl[INITIAL_STACK_SIZE];
/** nil value of the current element */
private boolean fNil;
/** nil value stack */
private boolean[] fNilStack = new boolean[INITIAL_STACK_SIZE];
/** notation value of the current element */
private XSNotationDecl fNotation;
/** notation stack */
private XSNotationDecl[] fNotationStack = new XSNotationDecl[INITIAL_STACK_SIZE];
/** Current type. */
private XSTypeDefinition fCurrentType;
/** type stack. */
private XSTypeDefinition[] fTypeStack = new XSTypeDefinition[INITIAL_STACK_SIZE];
/** Current content model. */
private XSCMValidator fCurrentCM;
/** Content model stack. */
private XSCMValidator[] fCMStack = new XSCMValidator[INITIAL_STACK_SIZE];
/** the current state of the current content model */
private int[] fCurrCMState;
/** stack to hold content model states */
private int[][] fCMStateStack = new int[INITIAL_STACK_SIZE][];
/** whether the curret element is strictly assessed */
private boolean fStrictAssess = true;
/** strict assess stack */
private boolean[] fStrictAssessStack = new boolean[INITIAL_STACK_SIZE];
/** Temporary string buffers. */
private final StringBuffer fBuffer = new StringBuffer();
/** Whether need to append characters to fBuffer */
private boolean fAppendBuffer = true;
/** Did we see any character data? */
private boolean fSawText = false;
/** stack to record if we saw character data */
private boolean[] fSawTextStack = new boolean[INITIAL_STACK_SIZE];
/** Did we see non-whitespace character data? */
private boolean fSawCharacters = false;
/** Stack to record if we saw character data outside of element content*/
private boolean[] fStringContent = new boolean[INITIAL_STACK_SIZE];
/** temporary qname */
private final QName fTempQName = new QName();
/** value of the "root-type-definition" property. */
private javax.xml.namespace.QName fRootTypeQName = null;
private XSTypeDefinition fRootTypeDefinition = null;
/** value of the "root-element-declaration" property. */
private javax.xml.namespace.QName fRootElementDeclQName = null;
private XSElementDecl fRootElementDeclaration = null;
private int fIgnoreXSITypeDepth;
private boolean fIDCChecking;
/** temporary validated info */
private ValidatedInfo fValidatedInfo = new ValidatedInfo();
// used to validate default/fixed values against xsi:type  
// only need to check facets, so we set extraChecking to false (in reset)  
private ValidationState fState4XsiType = new ValidationState();
// used to apply default/fixed values  
// only need to check id/idref/entity, so we set checkFacets to false  
private ValidationState fState4ApplyDefault = new ValidationState();
// identity constraint information  
/**
     * Stack of active XPath matchers for identity constraints. All
     * active XPath matchers are notified of startElement
     * and endElement callbacks in order to perform their matches.
     * <p>
     * For each element with identity constraints, the selector of
     * each identity constraint is activated. When the selector matches
     * its XPath, then all the fields of the identity constraint are
     * activated.
     * <p>
     * <strong>Note:</strong> Once the activation scope is left, the
     * XPath matchers are automatically removed from the stack of
     * active matchers and no longer receive callbacks.
     */
protected XPathMatcherStack fMatcherStack = new XPathMatcherStack();
/** Cache of value stores for identity constraint fields. */
protected ValueStoreCache fValueStoreCache = new ValueStoreCache();
}
