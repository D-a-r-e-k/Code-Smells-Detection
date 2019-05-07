public void reset(XMLComponentManager componentManager) {
    // set symbol table  
    fSymbolTable = (SymbolTable) componentManager.getProperty(SYMBOL_TABLE);
    //set entity resolver  
    fEntityResolver = (XMLEntityResolver) componentManager.getProperty(ENTITY_MANAGER);
    XMLEntityResolver er = (XMLEntityResolver) componentManager.getProperty(ENTITY_RESOLVER);
    if (er != null)
        fSchemaParser.setEntityResolver(er);
    // set error reporter  
    fErrorReporter = (XMLErrorReporter) componentManager.getProperty(ERROR_REPORTER);
    try {
        XMLErrorHandler currErrorHandler = fErrorReporter.getErrorHandler();
        // Setting a parser property can be much more expensive  
        // than checking its value.  Don't set the ERROR_HANDLER  
        // or LOCALE properties unless they've actually changed.  
        if (currErrorHandler != fSchemaParser.getProperty(ERROR_HANDLER)) {
            fSchemaParser.setProperty(ERROR_HANDLER, (currErrorHandler != null) ? currErrorHandler : new DefaultErrorHandler());
            if (fAnnotationValidator != null) {
                fAnnotationValidator.setProperty(ERROR_HANDLER, (currErrorHandler != null) ? currErrorHandler : new DefaultErrorHandler());
            }
        }
        Locale currentLocale = fErrorReporter.getLocale();
        if (currentLocale != fSchemaParser.getProperty(LOCALE)) {
            fSchemaParser.setProperty(LOCALE, currentLocale);
            if (fAnnotationValidator != null) {
                fAnnotationValidator.setProperty(LOCALE, currentLocale);
            }
        }
    } catch (XMLConfigurationException e) {
    }
    try {
        fValidateAnnotations = componentManager.getFeature(VALIDATE_ANNOTATIONS);
    } catch (XMLConfigurationException e) {
        fValidateAnnotations = false;
    }
    try {
        fHonourAllSchemaLocations = componentManager.getFeature(HONOUR_ALL_SCHEMALOCATIONS);
    } catch (XMLConfigurationException e) {
        fHonourAllSchemaLocations = false;
    }
    try {
        fNamespaceGrowth = componentManager.getFeature(NAMESPACE_GROWTH);
    } catch (XMLConfigurationException e) {
        fNamespaceGrowth = false;
    }
    try {
        fTolerateDuplicates = componentManager.getFeature(TOLERATE_DUPLICATES);
    } catch (XMLConfigurationException e) {
        fTolerateDuplicates = false;
    }
    try {
        fSchemaParser.setFeature(CONTINUE_AFTER_FATAL_ERROR, fErrorReporter.getFeature(CONTINUE_AFTER_FATAL_ERROR));
    } catch (XMLConfigurationException e) {
    }
    try {
        fSchemaParser.setFeature(ALLOW_JAVA_ENCODINGS, componentManager.getFeature(ALLOW_JAVA_ENCODINGS));
    } catch (XMLConfigurationException e) {
    }
    try {
        fSchemaParser.setFeature(STANDARD_URI_CONFORMANT_FEATURE, componentManager.getFeature(STANDARD_URI_CONFORMANT_FEATURE));
    } catch (XMLConfigurationException e) {
    }
    try {
        fGrammarPool = (XMLGrammarPool) componentManager.getProperty(XMLGRAMMAR_POOL);
    } catch (XMLConfigurationException e) {
        fGrammarPool = null;
    }
    // security features  
    try {
        fSchemaParser.setFeature(DISALLOW_DOCTYPE, componentManager.getFeature(DISALLOW_DOCTYPE));
    } catch (XMLConfigurationException e) {
    }
    try {
        Object security = componentManager.getProperty(SECURITY_MANAGER);
        if (security != null) {
            fSchemaParser.setProperty(SECURITY_MANAGER, security);
        }
    } catch (XMLConfigurationException e) {
    }
}
