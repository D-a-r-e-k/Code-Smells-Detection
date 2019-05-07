void method0() { 
//  
// Constants  
//  
private static final char[] XML11_VERSION = new char[] { '1', '.', '1' };
// property identifiers  
/** Property identifier: symbol table. */
protected static final String SYMBOL_TABLE = Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;
/** Property identifier: error reporter. */
protected static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;
/** Property identifier: entity manager. */
protected static final String ENTITY_MANAGER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY;
//  
// Data  
//  
/** Symbol: "version". */
protected static final String fVersionSymbol = "version".intern();
// symbol:  [xml]:  
protected static final String fXMLSymbol = "[xml]".intern();
/** Symbol table. */
protected SymbolTable fSymbolTable;
/** Error reporter. */
protected XMLErrorReporter fErrorReporter;
/** Entity manager. */
protected XMLEntityManager fEntityManager;
protected String fEncoding = null;
private final char[] fExpectedVersionString = { '<', '?', 'x', 'm', 'l', ' ', 'v', 'e', 'r', 's', 'i', 'o', 'n', '=', ' ', ' ', ' ', ' ', ' ' };
}
