void method0() { 
/** Feature identifier: validation. */
protected static final String VALIDATION = Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE;
/** feature identifier: XML Schema validation */
protected static final String XMLSCHEMA_VALIDATION = Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_VALIDATION_FEATURE;
/** Feature identifier:  allow java encodings */
protected static final String ALLOW_JAVA_ENCODINGS = Constants.XERCES_FEATURE_PREFIX + Constants.ALLOW_JAVA_ENCODINGS_FEATURE;
/** Feature identifier:  continue after fatal error */
protected static final String CONTINUE_AFTER_FATAL_ERROR = Constants.XERCES_FEATURE_PREFIX + Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE;
/** Feature identifier:  allow java encodings */
protected static final String STANDARD_URI_CONFORMANT_FEATURE = Constants.XERCES_FEATURE_PREFIX + Constants.STANDARD_URI_CONFORMANT_FEATURE;
/** Feature: disallow doctype*/
protected static final String DISALLOW_DOCTYPE = Constants.XERCES_FEATURE_PREFIX + Constants.DISALLOW_DOCTYPE_DECL_FEATURE;
/** Feature: generate synthetic annotations */
protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = Constants.XERCES_FEATURE_PREFIX + Constants.GENERATE_SYNTHETIC_ANNOTATIONS_FEATURE;
/** Feature identifier: validate annotations. */
protected static final String VALIDATE_ANNOTATIONS = Constants.XERCES_FEATURE_PREFIX + Constants.VALIDATE_ANNOTATIONS_FEATURE;
/** Feature identifier: honour all schemaLocations */
protected static final String HONOUR_ALL_SCHEMALOCATIONS = Constants.XERCES_FEATURE_PREFIX + Constants.HONOUR_ALL_SCHEMALOCATIONS_FEATURE;
/** Feature identifier: namespace growth */
protected static final String NAMESPACE_GROWTH = Constants.XERCES_FEATURE_PREFIX + Constants.NAMESPACE_GROWTH_FEATURE;
/** Feature identifier: tolerate duplicates */
protected static final String TOLERATE_DUPLICATES = Constants.XERCES_FEATURE_PREFIX + Constants.TOLERATE_DUPLICATES_FEATURE;
/** Feature identifier: namespace prefixes. */
private static final String NAMESPACE_PREFIXES = Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACE_PREFIXES_FEATURE;
/** Feature identifier: string interning. */
protected static final String STRING_INTERNING = Constants.SAX_FEATURE_PREFIX + Constants.STRING_INTERNING_FEATURE;
/** Property identifier: error handler. */
protected static final String ERROR_HANDLER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_HANDLER_PROPERTY;
/** Property identifier: JAXP schema source. */
protected static final String JAXP_SCHEMA_SOURCE = Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_SOURCE;
/** Property identifier: entity resolver. */
public static final String ENTITY_RESOLVER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_RESOLVER_PROPERTY;
/** Property identifier: entity manager. */
protected static final String ENTITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY;
/** Property identifier: error reporter. */
public static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;
/** Property identifier: grammar pool. */
public static final String XMLGRAMMAR_POOL = Constants.XERCES_PROPERTY_PREFIX + Constants.XMLGRAMMAR_POOL_PROPERTY;
/** Property identifier: symbol table. */
public static final String SYMBOL_TABLE = Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;
/** Property identifier: security manager. */
protected static final String SECURITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.SECURITY_MANAGER_PROPERTY;
/** Property identifier: locale. */
protected static final String LOCALE = Constants.XERCES_PROPERTY_PREFIX + Constants.LOCALE_PROPERTY;
protected static final boolean DEBUG_NODE_POOL = false;
// Data  
// different sorts of declarations; should make lookup and  
// traverser calling more efficient/less bulky.  
static final int ATTRIBUTE_TYPE = 1;
static final int ATTRIBUTEGROUP_TYPE = 2;
static final int ELEMENT_TYPE = 3;
static final int GROUP_TYPE = 4;
static final int IDENTITYCONSTRAINT_TYPE = 5;
static final int NOTATION_TYPE = 6;
static final int TYPEDECL_TYPE = 7;
// this string gets appended to redefined names; it's purpose is to be  
// as unlikely as possible to cause collisions.  
public static final String REDEF_IDENTIFIER = "_fn3dktizrknc9pi";
//  
//protected data that can be accessable by any traverser  
// stores <notation> decl  
protected Hashtable fNotationRegistry = new Hashtable();
protected XSDeclarationPool fDeclPool = null;
// These tables correspond to the symbol spaces defined in the  
// spec.  
// They are keyed with a QName (that is, String("URI,localpart) and  
// their values are nodes corresponding to the given name's decl.  
// By asking the node for its ownerDocument and looking in  
// XSDocumentInfoRegistry we can easily get the corresponding  
// XSDocumentInfo object.  
private Hashtable fUnparsedAttributeRegistry = new Hashtable();
private Hashtable fUnparsedAttributeGroupRegistry = new Hashtable();
private Hashtable fUnparsedElementRegistry = new Hashtable();
private Hashtable fUnparsedGroupRegistry = new Hashtable();
private Hashtable fUnparsedIdentityConstraintRegistry = new Hashtable();
private Hashtable fUnparsedNotationRegistry = new Hashtable();
private Hashtable fUnparsedTypeRegistry = new Hashtable();
// Compensation for the above hashtables to locate XSDocumentInfo,   
// Since we may take Schema Element directly, so can not get the  
// corresponding XSDocumentInfo object just using above hashtables.  
private Hashtable fUnparsedAttributeRegistrySub = new Hashtable();
private Hashtable fUnparsedAttributeGroupRegistrySub = new Hashtable();
private Hashtable fUnparsedElementRegistrySub = new Hashtable();
private Hashtable fUnparsedGroupRegistrySub = new Hashtable();
private Hashtable fUnparsedIdentityConstraintRegistrySub = new Hashtable();
private Hashtable fUnparsedNotationRegistrySub = new Hashtable();
private Hashtable fUnparsedTypeRegistrySub = new Hashtable();
// Stores XSDocumentInfo (keyed by component name), to check for duplicate  
// components declared within the same xsd document  
private Hashtable fUnparsedRegistriesExt[] = new Hashtable[] { null, new Hashtable(), // ATTRIBUTE_TYPE  
new Hashtable(), // ATTRIBUTEGROUP_TYPE  
new Hashtable(), // ELEMENT_TYPE  
new Hashtable(), // GROUP_TYPE  
new Hashtable(), // IDENTITYCONSTRAINT_TYPE  
new Hashtable(), // NOTATION_TYPE  
new Hashtable() };
// this is keyed with a documentNode (or the schemaRoot nodes  
// contained in the XSDocumentInfo objects) and its value is the  
// XSDocumentInfo object corresponding to that document.  
// Basically, the function of this registry is to be a link  
// between the nodes we fetch from calls to the fUnparsed*  
// arrays and the XSDocumentInfos they live in.  
private Hashtable fXSDocumentInfoRegistry = new Hashtable();
// this hashtable is keyed on by XSDocumentInfo objects.  Its values  
// are Vectors containing the XSDocumentInfo objects <include>d,  
// <import>ed or <redefine>d by the key XSDocumentInfo.  
private Hashtable fDependencyMap = new Hashtable();
// this hashtable is keyed on by a target namespace.  Its values  
// are Vectors containing namespaces imported by schema documents  
// with the key target namespace.  
// if an imprted schema has absent namespace, the value "null" is stored.  
private Hashtable fImportMap = new Hashtable();
// all namespaces that imports other namespaces  
// if the importing schema has absent namespace, empty string is stored.  
// (because the key of a hashtable can't be null.)  
private Vector fAllTNSs = new Vector();
// stores instance document mappings between namespaces and schema hints  
private Hashtable fLocationPairs = null;
private static final Hashtable EMPTY_TABLE = new Hashtable();
// Records which nodes are hidden when the input is a DOMInputSource.  
Hashtable fHiddenNodes = null;
// This vector stores strings which are combinations of the  
// publicId and systemId of the inputSource corresponding to a  
// schema document.  This combination is used so that the user's  
// EntityResolver can provide a consistent way of identifying a  
// schema document that is included in multiple other schemas.  
private Hashtable fTraversed = new Hashtable();
// this hashtable contains a mapping from Schema Element to its systemId  
// this is useful to resolve a uri relative to the referring document  
private Hashtable fDoc2SystemId = new Hashtable();
// the primary XSDocumentInfo we were called to parse  
private XSDocumentInfo fRoot = null;
// This hashtable's job is to act as a link between the Schema Element and its  
// XSDocumentInfo object.  
private Hashtable fDoc2XSDocumentMap = new Hashtable();
// map between <redefine> elements and the XSDocumentInfo  
// objects that correspond to the documents being redefined.  
private Hashtable fRedefine2XSDMap = new Hashtable();
// map between <redefine> elements and the namespace support  
private Hashtable fRedefine2NSSupport = new Hashtable();
// these objects store a mapping between the names of redefining  
// groups/attributeGroups and the groups/AttributeGroups which  
// they redefine by restriction (implicitly).  It is up to the  
// Group and AttributeGroup traversers to check these restrictions for  
// validity.  
private Hashtable fRedefinedRestrictedAttributeGroupRegistry = new Hashtable();
private Hashtable fRedefinedRestrictedGroupRegistry = new Hashtable();
// a variable storing whether the last schema document  
// processed (by getSchema) was a duplicate.  
private boolean fLastSchemaWasDuplicate;
// validate annotations feature  
private boolean fValidateAnnotations = false;
//handle multiple import feature  
private boolean fHonourAllSchemaLocations = false;
//handle namespace growth feature  
boolean fNamespaceGrowth = false;
// handle tolerate duplicates feature  
boolean fTolerateDuplicates = false;
// the XMLErrorReporter  
private XMLErrorReporter fErrorReporter;
private XMLEntityResolver fEntityResolver;
// the XSAttributeChecker  
private XSAttributeChecker fAttributeChecker;
// the symbol table  
private SymbolTable fSymbolTable;
// the GrammarResolver  
private XSGrammarBucket fGrammarBucket;
// the Grammar description  
private XSDDescription fSchemaGrammarDescription;
// the Grammar Pool  
private XMLGrammarPool fGrammarPool;
//************ Traversers **********  
XSDAttributeGroupTraverser fAttributeGroupTraverser;
XSDAttributeTraverser fAttributeTraverser;
XSDComplexTypeTraverser fComplexTypeTraverser;
XSDElementTraverser fElementTraverser;
XSDGroupTraverser fGroupTraverser;
XSDKeyrefTraverser fKeyrefTraverser;
XSDNotationTraverser fNotationTraverser;
XSDSimpleTypeTraverser fSimpleTypeTraverser;
XSDUniqueOrKeyTraverser fUniqueOrKeyTraverser;
XSDWildcardTraverser fWildCardTraverser;
SchemaDVFactory fDVFactory;
SchemaDOMParser fSchemaParser;
SchemaContentHandler fXSContentHandler;
StAXSchemaParser fStAXSchemaParser;
XML11Configuration fAnnotationValidator;
XSAnnotationGrammarPool fGrammarBucketAdapter;
// these data members are needed for the deferred traversal  
// of local elements.  
// the initial size of the array to store deferred local elements  
private static final int INIT_STACK_SIZE = 30;
// the incremental size of the array to store deferred local elements  
private static final int INC_STACK_SIZE = 10;
// current position of the array (# of deferred local elements)  
private int fLocalElemStackPos = 0;
private XSParticleDecl[] fParticle = new XSParticleDecl[INIT_STACK_SIZE];
private Element[] fLocalElementDecl = new Element[INIT_STACK_SIZE];
private XSDocumentInfo[] fLocalElementDecl_schema = new XSDocumentInfo[INIT_STACK_SIZE];
//JACK  
private int[] fAllContext = new int[INIT_STACK_SIZE];
private XSObject[] fParent = new XSObject[INIT_STACK_SIZE];
private String[][] fLocalElemNamespaceContext = new String[INIT_STACK_SIZE][1];
// these data members are needed for the deferred traversal  
// of keyrefs.  
// the initial size of the array to store deferred keyrefs  
private static final int INIT_KEYREF_STACK = 2;
// the incremental size of the array to store deferred keyrefs  
private static final int INC_KEYREF_STACK_AMOUNT = 2;
// current position of the array (# of deferred keyrefs)  
private int fKeyrefStackPos = 0;
private Element[] fKeyrefs = new Element[INIT_KEYREF_STACK];
private XSDocumentInfo[] fKeyrefsMapXSDocumentInfo = new XSDocumentInfo[INIT_KEYREF_STACK];
private XSElementDecl[] fKeyrefElems = new XSElementDecl[INIT_KEYREF_STACK];
private String[][] fKeyrefNamespaceContext = new String[INIT_KEYREF_STACK][1];
// global decls: map from decl name to decl object  
SymbolHash fGlobalAttrDecls = new SymbolHash();
SymbolHash fGlobalAttrGrpDecls = new SymbolHash();
SymbolHash fGlobalElemDecls = new SymbolHash();
SymbolHash fGlobalGroupDecls = new SymbolHash();
SymbolHash fGlobalNotationDecls = new SymbolHash();
SymbolHash fGlobalIDConstraintDecls = new SymbolHash();
SymbolHash fGlobalTypeDecls = new SymbolHash();
// may wish to have setter methods for ErrorHandler,  
// EntityResolver...  
private static final String[][] NS_ERROR_CODES = { { "src-include.2.1", "src-include.2.1" }, { "src-redefine.3.1", "src-redefine.3.1" }, { "src-import.3.1", "src-import.3.2" }, null, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" }, { "TargetNamespace.1", "TargetNamespace.2" } };
private static final String[] ELE_ERROR_CODES = { "src-include.1", "src-redefine.2", "src-import.2", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4", "schema_reference.4" };
// end traverseSchemas  
// store whether we have reported an error about that no grammar  
// is found for the given namespace uri  
private Vector fReportedTNS = null;
private static final String[] COMP_TYPE = { null, // index 0  
"attribute declaration", "attribute group", "element declaration", "group", "identity constraint", "notation", "type definition" };
private static final String[] CIRCULAR_CODES = { "Internal-Error", "Internal-Error", "src-attribute_group.3", "e-props-correct.6", "mg-props-correct.2", "Internal-Error", "Internal-Error", "st-props-correct.2" };
// setSchemasVisible(XSDocumentInfo): void  
private SimpleLocator xl = new SimpleLocator();
}
