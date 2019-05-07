// <init>()  
//  
// XMLComponent methods  
//  
/*
     * Resets the component. The component can query the component manager
     * about any features and properties that affect the operation of the
     * component.
     * 
     * @param componentManager The component manager.
     *
     * @throws SAXException Thrown by component on finitialization error.
     *                      For example, if a feature or property is
     *                      required for the operation of the component, the
     *                      component manager may throw a 
     *                      SAXNotRecognizedException or a
     *                      SAXNotSupportedException.
     */
public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
    boolean parser_settings;
    try {
        parser_settings = componentManager.getFeature(PARSER_SETTINGS);
    } catch (XMLConfigurationException e) {
        parser_settings = true;
    }
    if (!parser_settings) {
        // parser settings have not been changed  
        reset();
        return;
    }
    // sax features  
    try {
        fValidation = componentManager.getFeature(VALIDATION);
    } catch (XMLConfigurationException e) {
        fValidation = false;
    }
    try {
        fDTDValidation = !(componentManager.getFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_VALIDATION_FEATURE));
    } catch (XMLConfigurationException e) {
        // must be in a schema-less configuration!  
        fDTDValidation = true;
    }
    // Xerces features  
    try {
        fWarnDuplicateAttdef = componentManager.getFeature(WARN_ON_DUPLICATE_ATTDEF);
    } catch (XMLConfigurationException e) {
        fWarnDuplicateAttdef = false;
    }
    try {
        fWarnOnUndeclaredElemdef = componentManager.getFeature(WARN_ON_UNDECLARED_ELEMDEF);
    } catch (XMLConfigurationException e) {
        fWarnOnUndeclaredElemdef = false;
    }
    // get needed components  
    fErrorReporter = (XMLErrorReporter) componentManager.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY);
    fSymbolTable = (SymbolTable) componentManager.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY);
    try {
        fGrammarPool = (XMLGrammarPool) componentManager.getProperty(GRAMMAR_POOL);
    } catch (XMLConfigurationException e) {
        fGrammarPool = null;
    }
    try {
        fValidator = (XMLDTDValidator) componentManager.getProperty(DTD_VALIDATOR);
    } catch (XMLConfigurationException e) {
        fValidator = null;
    } catch (ClassCastException e) {
        fValidator = null;
    }
    // we get our grammarBucket from the validator...  
    if (fValidator != null) {
        fGrammarBucket = fValidator.getGrammarBucket();
    } else {
        fGrammarBucket = null;
    }
    reset();
}
