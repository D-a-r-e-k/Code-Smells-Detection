// getGrammarBucket():  DTDGrammarBucket  
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
    // clear grammars  
    fDTDGrammar = null;
    fSeenDoctypeDecl = false;
    fInCDATASection = false;
    // initialize state  
    fSeenRootElement = false;
    fInElementContent = false;
    fCurrentElementIndex = -1;
    fCurrentContentSpecType = -1;
    fRootElement.clear();
    fValidationState.resetIDTables();
    fGrammarBucket.clear();
    fElementDepth = -1;
    fElementChildrenLength = 0;
    boolean parser_settings;
    try {
        parser_settings = componentManager.getFeature(PARSER_SETTINGS);
    } catch (XMLConfigurationException e) {
        parser_settings = true;
    }
    if (!parser_settings) {
        // parser settings have not been changed  
        fValidationManager.addValidationState(fValidationState);
        return;
    }
    // sax features  
    try {
        fNamespaces = componentManager.getFeature(NAMESPACES);
    } catch (XMLConfigurationException e) {
        fNamespaces = true;
    }
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
        fDynamicValidation = componentManager.getFeature(DYNAMIC_VALIDATION);
    } catch (XMLConfigurationException e) {
        fDynamicValidation = false;
    }
    try {
        fBalanceSyntaxTrees = componentManager.getFeature(BALANCE_SYNTAX_TREES);
    } catch (XMLConfigurationException e) {
        fBalanceSyntaxTrees = false;
    }
    try {
        fWarnDuplicateAttdef = componentManager.getFeature(WARN_ON_DUPLICATE_ATTDEF);
    } catch (XMLConfigurationException e) {
        fWarnDuplicateAttdef = false;
    }
    try {
        fSchemaType = (String) componentManager.getProperty(Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_LANGUAGE);
    } catch (XMLConfigurationException e) {
        fSchemaType = null;
    }
    fValidationManager = (ValidationManager) componentManager.getProperty(VALIDATION_MANAGER);
    fValidationManager.addValidationState(fValidationState);
    fValidationState.setUsingNamespaces(fNamespaces);
    // get needed components  
    fErrorReporter = (XMLErrorReporter) componentManager.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY);
    fSymbolTable = (SymbolTable) componentManager.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY);
    try {
        fGrammarPool = (XMLGrammarPool) componentManager.getProperty(GRAMMAR_POOL);
    } catch (XMLConfigurationException e) {
        fGrammarPool = null;
    }
    fDatatypeValidatorFactory = (DTDDVFactory) componentManager.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.DATATYPE_VALIDATOR_FACTORY_PROPERTY);
    init();
}
