//  
// XMLComponent methods  
//  
/**
     * Resets the component. The component can query the component manager
     * about any features and properties that affect the operation of the
     * component.
     *
     * @param componentManager The component manager.
     *
     * @throws SAXException Thrown by component on initialization error.
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
        fExternalGeneralEntities = componentManager.getFeature(EXTERNAL_GENERAL_ENTITIES);
    } catch (XMLConfigurationException e) {
        fExternalGeneralEntities = true;
    }
    try {
        fExternalParameterEntities = componentManager.getFeature(EXTERNAL_PARAMETER_ENTITIES);
    } catch (XMLConfigurationException e) {
        fExternalParameterEntities = true;
    }
    // xerces features  
    try {
        fAllowJavaEncodings = componentManager.getFeature(ALLOW_JAVA_ENCODINGS);
    } catch (XMLConfigurationException e) {
        fAllowJavaEncodings = false;
    }
    try {
        fWarnDuplicateEntityDef = componentManager.getFeature(WARN_ON_DUPLICATE_ENTITYDEF);
    } catch (XMLConfigurationException e) {
        fWarnDuplicateEntityDef = false;
    }
    try {
        fStrictURI = componentManager.getFeature(STANDARD_URI_CONFORMANT);
    } catch (XMLConfigurationException e) {
        fStrictURI = false;
    }
    // xerces properties  
    fSymbolTable = (SymbolTable) componentManager.getProperty(SYMBOL_TABLE);
    fErrorReporter = (XMLErrorReporter) componentManager.getProperty(ERROR_REPORTER);
    try {
        fEntityResolver = (XMLEntityResolver) componentManager.getProperty(ENTITY_RESOLVER);
    } catch (XMLConfigurationException e) {
        fEntityResolver = null;
    }
    try {
        fValidationManager = (ValidationManager) componentManager.getProperty(VALIDATION_MANAGER);
    } catch (XMLConfigurationException e) {
        fValidationManager = null;
    }
    try {
        fSecurityManager = (SecurityManager) componentManager.getProperty(SECURITY_MANAGER);
    } catch (XMLConfigurationException e) {
        fSecurityManager = null;
    }
    // reset general state  
    reset();
}
