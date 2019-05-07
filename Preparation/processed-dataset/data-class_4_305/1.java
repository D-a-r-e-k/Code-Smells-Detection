//  
// XMLComponent methods  
//  
/**
     * 
     * 
     * @param componentManager The component manager.
     *
     * @throws SAXException Throws exception if required features and
     *                      properties cannot be found.
     */
public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
    try {
        fParserSettings = componentManager.getFeature(PARSER_SETTINGS);
    } catch (XMLConfigurationException e) {
        fParserSettings = true;
    }
    if (!fParserSettings) {
        // parser settings have not been changed  
        init();
        return;
    }
    // Xerces properties  
    fSymbolTable = (SymbolTable) componentManager.getProperty(SYMBOL_TABLE);
    fErrorReporter = (XMLErrorReporter) componentManager.getProperty(ERROR_REPORTER);
    fEntityManager = (XMLEntityManager) componentManager.getProperty(ENTITY_MANAGER);
    // sax features  
    try {
        fValidation = componentManager.getFeature(VALIDATION);
    } catch (XMLConfigurationException e) {
        fValidation = false;
    }
    try {
        fNamespaces = componentManager.getFeature(NAMESPACES);
    } catch (XMLConfigurationException e) {
        fNamespaces = true;
    }
    try {
        fNotifyCharRefs = componentManager.getFeature(NOTIFY_CHAR_REFS);
    } catch (XMLConfigurationException e) {
        fNotifyCharRefs = false;
    }
    init();
}
