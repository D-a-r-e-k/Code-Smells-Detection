// scanDocument(boolean):boolean  
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
    super.reset(componentManager);
    // other settings  
    //fDocumentSystemId = null;  
    // sax features  
    fAttributes.setNamespaces(fNamespaces);
    // initialize vars  
    fMarkupDepth = 0;
    fCurrentElement = null;
    fElementStack.clear();
    fHasExternalDTD = false;
    fStandalone = false;
    fIsEntityDeclaredVC = false;
    fInScanContent = false;
    // setup dispatcher  
    setScannerState(SCANNER_STATE_CONTENT);
    setDispatcher(fContentDispatcher);
    if (fParserSettings) {
        // parser settings have changed. reset them.  
        // xerces features  
        try {
            fNotifyBuiltInRefs = componentManager.getFeature(NOTIFY_BUILTIN_REFS);
        } catch (XMLConfigurationException e) {
            fNotifyBuiltInRefs = false;
        }
        // xerces properties  
        try {
            Object resolver = componentManager.getProperty(ENTITY_RESOLVER);
            fExternalSubsetResolver = (resolver instanceof ExternalSubsetResolver) ? (ExternalSubsetResolver) resolver : null;
        } catch (XMLConfigurationException e) {
            fExternalSubsetResolver = null;
        }
    }
}
