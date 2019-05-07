void method0() { 
//  
// Constants  
//  
/** Default buffer size (2048). */
public static final int DEFAULT_BUFFER_SIZE = 2048;
/** Default buffer size before we've finished with the XMLDecl:  */
public static final int DEFAULT_XMLDECL_BUFFER_SIZE = 64;
/** Default internal entity buffer size (512). */
public static final int DEFAULT_INTERNAL_BUFFER_SIZE = 512;
// feature identifiers  
/** Feature identifier: validation. */
protected static final String VALIDATION = Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE;
/** Feature identifier: external general entities. */
protected static final String EXTERNAL_GENERAL_ENTITIES = Constants.SAX_FEATURE_PREFIX + Constants.EXTERNAL_GENERAL_ENTITIES_FEATURE;
/** Feature identifier: external parameter entities. */
protected static final String EXTERNAL_PARAMETER_ENTITIES = Constants.SAX_FEATURE_PREFIX + Constants.EXTERNAL_PARAMETER_ENTITIES_FEATURE;
/** Feature identifier: allow Java encodings. */
protected static final String ALLOW_JAVA_ENCODINGS = Constants.XERCES_FEATURE_PREFIX + Constants.ALLOW_JAVA_ENCODINGS_FEATURE;
/** Feature identifier: warn on duplicate EntityDef */
protected static final String WARN_ON_DUPLICATE_ENTITYDEF = Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_DUPLICATE_ENTITYDEF_FEATURE;
/** Feature identifier: standard uri conformant */
protected static final String STANDARD_URI_CONFORMANT = Constants.XERCES_FEATURE_PREFIX + Constants.STANDARD_URI_CONFORMANT_FEATURE;
protected static final String PARSER_SETTINGS = Constants.XERCES_FEATURE_PREFIX + Constants.PARSER_SETTINGS;
// property identifiers  
/** Property identifier: symbol table. */
protected static final String SYMBOL_TABLE = Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;
/** Property identifier: error reporter. */
protected static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;
/** Property identifier: entity resolver. */
protected static final String ENTITY_RESOLVER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_RESOLVER_PROPERTY;
// property identifier:  ValidationManager  
protected static final String VALIDATION_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.VALIDATION_MANAGER_PROPERTY;
/** property identifier: buffer size. */
protected static final String BUFFER_SIZE = Constants.XERCES_PROPERTY_PREFIX + Constants.BUFFER_SIZE_PROPERTY;
/** property identifier: security manager. */
protected static final String SECURITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.SECURITY_MANAGER_PROPERTY;
// recognized features and properties  
/** Recognized features. */
private static final String[] RECOGNIZED_FEATURES = { VALIDATION, EXTERNAL_GENERAL_ENTITIES, EXTERNAL_PARAMETER_ENTITIES, ALLOW_JAVA_ENCODINGS, WARN_ON_DUPLICATE_ENTITYDEF, STANDARD_URI_CONFORMANT };
/** Feature defaults. */
private static final Boolean[] FEATURE_DEFAULTS = { null, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
/** Recognized properties. */
private static final String[] RECOGNIZED_PROPERTIES = { SYMBOL_TABLE, ERROR_REPORTER, ENTITY_RESOLVER, VALIDATION_MANAGER, BUFFER_SIZE, SECURITY_MANAGER };
/** Property defaults. */
private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, new Integer(DEFAULT_BUFFER_SIZE), null };
private static final String XMLEntity = "[xml]".intern();
private static final String DTDEntity = "[dtd]".intern();
// debugging  
/**
     * Debug printing of buffer. This debugging flag works best when you
     * resize the DEFAULT_BUFFER_SIZE down to something reasonable like
     * 64 characters.
     */
private static final boolean DEBUG_BUFFER = false;
/** Debug some basic entities. */
private static final boolean DEBUG_ENTITIES = false;
/** Debug switching readers for encodings. */
private static final boolean DEBUG_ENCODINGS = false;
// should be diplayed trace resolving messages  
private static final boolean DEBUG_RESOLVER = false;
//  
// Data  
//  
// features  
/**
     * Validation. This feature identifier is:
     * http://xml.org/sax/features/validation
     */
protected boolean fValidation;
/**
     * External general entities. This feature identifier is:
     * http://xml.org/sax/features/external-general-entities
     */
protected boolean fExternalGeneralEntities = true;
/**
     * External parameter entities. This feature identifier is:
     * http://xml.org/sax/features/external-parameter-entities
     */
protected boolean fExternalParameterEntities = true;
/**
     * Allow Java encoding names. This feature identifier is:
     * http://apache.org/xml/features/allow-java-encodings
     */
protected boolean fAllowJavaEncodings;
/** warn on duplicate Entity declaration.
     *  http://apache.org/xml/features/warn-on-duplicate-entitydef
     */
protected boolean fWarnDuplicateEntityDef;
/**
     * standard uri conformant (strict uri).
     * http://apache.org/xml/features/standard-uri-conformant
     */
protected boolean fStrictURI;
// properties  
/**
     * Symbol table. This property identifier is:
     * http://apache.org/xml/properties/internal/symbol-table
     */
protected SymbolTable fSymbolTable;
/**
     * Error reporter. This property identifier is:
     * http://apache.org/xml/properties/internal/error-reporter
     */
protected XMLErrorReporter fErrorReporter;
/**
     * Entity resolver. This property identifier is:
     * http://apache.org/xml/properties/internal/entity-resolver
     */
protected XMLEntityResolver fEntityResolver;
/**
     * Validation manager. This property identifier is:
     * http://apache.org/xml/properties/internal/validation-manager
     */
protected ValidationManager fValidationManager;
// settings  
/**
     * Buffer size. We get this value from a property. The default size
     * is used if the input buffer size property is not specified.
     * REVISIT: do we need a property for internal entity buffer size?
     */
protected int fBufferSize = DEFAULT_BUFFER_SIZE;
// stores defaults for entity expansion limit if it has  
// been set on the configuration.  
protected SecurityManager fSecurityManager = null;
/**
     * True if the document entity is standalone. This should really
     * only be set by the document source (e.g. XMLDocumentScanner).
     */
protected boolean fStandalone;
/**
     * True if the current document contains parameter entity references.
     */
protected boolean fHasPEReferences;
// are the entities being parsed in the external subset?  
// NOTE:  this *is not* the same as whether they're external entities!  
protected boolean fInExternalSubset = false;
// handlers  
/** Entity handler. */
protected XMLEntityHandler fEntityHandler;
// scanner  
/** Current entity scanner. */
protected XMLEntityScanner fEntityScanner;
/** XML 1.0 entity scanner. */
protected XMLEntityScanner fXML10EntityScanner;
/** XML 1.1 entity scanner. */
protected XMLEntityScanner fXML11EntityScanner;
// entity expansion limit (contains useful data if and only if  
// fSecurityManager is non-null)  
protected int fEntityExpansionLimit = 0;
// entity currently being expanded:  
protected int fEntityExpansionCount = 0;
// entities  
/** Entities. */
protected final Hashtable fEntities = new Hashtable();
/** Entity stack. */
protected final Stack fEntityStack = new Stack();
/** Current entity. */
protected ScannedEntity fCurrentEntity;
// shared context  
/** Shared declared entities. */
protected Hashtable fDeclaredEntities;
// temp vars  
/** Resource identifier. */
private final XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();
/** Augmentations for entities. */
private final Augmentations fEntityAugs = new AugmentationsImpl();
/** Pool of byte buffers for single byte and variable width encodings, such as US-ASCII and UTF-8. */
private final ByteBufferPool fSmallByteBufferPool = new ByteBufferPool(fBufferSize);
/** Pool of byte buffers for 2-byte encodings, such as UTF-16. **/
private final ByteBufferPool fLargeByteBufferPool = new ByteBufferPool(fBufferSize << 1);
/** Temporary storage for the current entity's byte buffer. */
private byte[] fTempByteBuffer = null;
/** Pool of character buffers. */
private final CharacterBufferPool fCharacterBufferPool = new CharacterBufferPool(fBufferSize, DEFAULT_INTERNAL_BUFFER_SIZE);
// getEntityScanner():XMLEntityScanner  
// A stack containing all the open readers  
protected Stack fReaderStack = new Stack();
// getPropertyDefault(String):Object  
//  
// Public static methods  
//  
// current value of the "user.dir" property  
private static String gUserDir;
// cached URI object for the current value of the escaped "user.dir" property stored as a URI  
private static URI gUserDirURI;
// which ASCII characters need to be escaped  
private static final boolean gNeedEscaping[] = new boolean[128];
// the first hex character if a character needs to be escaped  
private static final char gAfterEscaping1[] = new char[128];
// the second hex character if a character needs to be escaped  
private static final char gAfterEscaping2[] = new char[128];
private static final char[] gHexChs = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
private static PrivilegedAction GET_USER_DIR_SYSTEM_PROPERTY = new PrivilegedAction() {

    public Object run() {
        return System.getProperty("user.dir");
    }
};
}
