void method0() { 
//  
// Constants  
//  
/** Top level scope (-1). */
private static final int TOP_LEVEL_SCOPE = -1;
// feature identifiers  
/** Feature identifier: validation. */
protected static final String VALIDATION = Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE;
/** Feature identifier: notify character references. */
protected static final String NOTIFY_CHAR_REFS = Constants.XERCES_FEATURE_PREFIX + Constants.NOTIFY_CHAR_REFS_FEATURE;
/** Feature identifier: warn on duplicate attdef */
protected static final String WARN_ON_DUPLICATE_ATTDEF = Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_DUPLICATE_ATTDEF_FEATURE;
/** Feature identifier: warn on undeclared element referenced in content model. */
protected static final String WARN_ON_UNDECLARED_ELEMDEF = Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_UNDECLARED_ELEMDEF_FEATURE;
protected static final String PARSER_SETTINGS = Constants.XERCES_FEATURE_PREFIX + Constants.PARSER_SETTINGS;
// property identifiers  
/** Property identifier: symbol table. */
protected static final String SYMBOL_TABLE = Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;
/** Property identifier: error reporter. */
protected static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;
/** Property identifier: grammar pool. */
protected static final String GRAMMAR_POOL = Constants.XERCES_PROPERTY_PREFIX + Constants.XMLGRAMMAR_POOL_PROPERTY;
/** Property identifier: validator . */
protected static final String DTD_VALIDATOR = Constants.XERCES_PROPERTY_PREFIX + Constants.DTD_VALIDATOR_PROPERTY;
// recognized features and properties  
/** Recognized features. */
private static final String[] RECOGNIZED_FEATURES = { VALIDATION, WARN_ON_DUPLICATE_ATTDEF, WARN_ON_UNDECLARED_ELEMDEF, NOTIFY_CHAR_REFS };
/** Feature defaults. */
private static final Boolean[] FEATURE_DEFAULTS = { null, Boolean.FALSE, Boolean.FALSE, null };
/** Recognized properties. */
private static final String[] RECOGNIZED_PROPERTIES = { SYMBOL_TABLE, ERROR_REPORTER, GRAMMAR_POOL, DTD_VALIDATOR };
/** Property defaults. */
private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null };
// debugging  
//          
// Data  
//  
// features  
/** Validation. */
protected boolean fValidation;
/** Validation against only DTD */
protected boolean fDTDValidation;
/** warn on duplicate attribute definition, this feature works only when validation is true */
protected boolean fWarnDuplicateAttdef;
/** warn on undeclared element referenced in content model, this feature only works when valiation is true */
protected boolean fWarnOnUndeclaredElemdef;
// properties  
/** Symbol table. */
protected SymbolTable fSymbolTable;
/** Error reporter. */
protected XMLErrorReporter fErrorReporter;
/** Grammar bucket. */
protected DTDGrammarBucket fGrammarBucket;
// the validator to which we look for our grammar bucket (the  
// validator needs to hold the bucket so that it can initialize  
// the grammar with details like whether it's for a standalone document...  
protected XMLDTDValidator fValidator;
// the grammar pool we'll try to add the grammar to:  
protected XMLGrammarPool fGrammarPool;
// what's our Locale?  
protected Locale fLocale;
// handlers  
/** DTD handler. */
protected XMLDTDHandler fDTDHandler;
/** DTD source. */
protected XMLDTDSource fDTDSource;
/** DTD content model handler. */
protected XMLDTDContentModelHandler fDTDContentModelHandler;
/** DTD content model source. */
protected XMLDTDContentModelSource fDTDContentModelSource;
// grammars  
/** DTD Grammar. */
protected DTDGrammar fDTDGrammar;
// state  
/** Perform validation. */
private boolean fPerformValidation;
/** True if in an ignore conditional section of the DTD. */
protected boolean fInDTDIgnore;
// information regarding the current element  
// validation states  
/** Mixed. */
private boolean fMixed;
// temporary variables  
/** Temporary entity declaration. */
private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
/** Notation declaration hash. */
private final HashMap fNDataDeclNotations = new HashMap();
/** DTD element declaration name. */
private String fDTDElementDeclName = null;
/** Mixed element type "hash". */
private final ArrayList fMixedElementTypes = new ArrayList();
/** Element declarations in DTD. */
private final ArrayList fDTDElementDecls = new ArrayList();
// to check for duplicate ID or ANNOTATION attribute declare in  
// ATTLIST, and misc VCs  
/** ID attribute names. */
private HashMap fTableOfIDAttributeNames;
/** NOTATION attribute names. */
private HashMap fTableOfNOTATIONAttributeNames;
/** NOTATION enumeration values. */
private HashMap fNotationEnumVals;
}
