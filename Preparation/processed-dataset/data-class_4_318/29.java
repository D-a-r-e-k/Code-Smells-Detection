// <init>()  
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
    fIdConstraint = false;
    //reset XSDDescription  
    fLocationPairs.clear();
    fExpandedLocationPairs.clear();
    // cleanup id table  
    fValidationState.resetIDTables();
    // reset schema loader  
    fSchemaLoader.reset(componentManager);
    // initialize state  
    fCurrentElemDecl = null;
    fCurrentCM = null;
    fCurrCMState = null;
    fSkipValidationDepth = -1;
    fNFullValidationDepth = -1;
    fNNoneValidationDepth = -1;
    fElementDepth = -1;
    fSubElement = false;
    fSchemaDynamicValidation = false;
    // datatype normalization  
    fEntityRef = false;
    fInCDATA = false;
    fMatcherStack.clear();
    // get error reporter  
    fXSIErrorReporter.reset((XMLErrorReporter) componentManager.getProperty(ERROR_REPORTER));
    boolean parser_settings;
    try {
        parser_settings = componentManager.getFeature(PARSER_SETTINGS);
    } catch (XMLConfigurationException e) {
        parser_settings = true;
    }
    if (!parser_settings) {
        // parser settings have not been changed  
        fValidationManager.addValidationState(fValidationState);
        // the node limit on the SecurityManager may have changed so need to refresh.  
        nodeFactory.reset();
        // Re-parse external schema location properties.  
        XMLSchemaLoader.processExternalHints(fExternalSchemas, fExternalNoNamespaceSchema, fLocationPairs, fXSIErrorReporter.fErrorReporter);
        return;
    }
    // pass the component manager to the factory..  
    nodeFactory.reset(componentManager);
    // get symbol table. if it's a new one, add symbols to it.  
    SymbolTable symbolTable = (SymbolTable) componentManager.getProperty(SYMBOL_TABLE);
    if (symbolTable != fSymbolTable) {
        fSymbolTable = symbolTable;
    }
    try {
        fNamespaceGrowth = componentManager.getFeature(NAMESPACE_GROWTH);
    } catch (XMLConfigurationException e) {
        fNamespaceGrowth = false;
    }
    try {
        fDynamicValidation = componentManager.getFeature(DYNAMIC_VALIDATION);
    } catch (XMLConfigurationException e) {
        fDynamicValidation = false;
    }
    if (fDynamicValidation) {
        fDoValidation = true;
    } else {
        try {
            fDoValidation = componentManager.getFeature(VALIDATION);
        } catch (XMLConfigurationException e) {
            fDoValidation = false;
        }
    }
    if (fDoValidation) {
        try {
            fDoValidation = componentManager.getFeature(XMLSchemaValidator.SCHEMA_VALIDATION);
        } catch (XMLConfigurationException e) {
        }
    }
    try {
        fFullChecking = componentManager.getFeature(SCHEMA_FULL_CHECKING);
    } catch (XMLConfigurationException e) {
        fFullChecking = false;
    }
    try {
        fNormalizeData = componentManager.getFeature(NORMALIZE_DATA);
    } catch (XMLConfigurationException e) {
        fNormalizeData = false;
    }
    try {
        fSchemaElementDefault = componentManager.getFeature(SCHEMA_ELEMENT_DEFAULT);
    } catch (XMLConfigurationException e) {
        fSchemaElementDefault = false;
    }
    try {
        fAugPSVI = componentManager.getFeature(SCHEMA_AUGMENT_PSVI);
    } catch (XMLConfigurationException e) {
        fAugPSVI = true;
    }
    try {
        fSchemaType = (String) componentManager.getProperty(Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_LANGUAGE);
    } catch (XMLConfigurationException e) {
        fSchemaType = null;
    }
    try {
        fUseGrammarPoolOnly = componentManager.getFeature(USE_GRAMMAR_POOL_ONLY);
    } catch (XMLConfigurationException e) {
        fUseGrammarPoolOnly = false;
    }
    fEntityResolver = (XMLEntityResolver) componentManager.getProperty(ENTITY_MANAGER);
    fValidationManager = (ValidationManager) componentManager.getProperty(VALIDATION_MANAGER);
    fValidationManager.addValidationState(fValidationState);
    fValidationState.setSymbolTable(fSymbolTable);
    try {
        final Object rootType = componentManager.getProperty(ROOT_TYPE_DEF);
        if (rootType == null) {
            fRootTypeQName = null;
            fRootTypeDefinition = null;
        } else if (rootType instanceof javax.xml.namespace.QName) {
            fRootTypeQName = (javax.xml.namespace.QName) rootType;
            fRootTypeDefinition = null;
        } else {
            fRootTypeDefinition = (XSTypeDefinition) rootType;
            fRootTypeQName = null;
        }
    } catch (XMLConfigurationException e) {
        fRootTypeQName = null;
        fRootTypeDefinition = null;
    }
    try {
        final Object rootDecl = componentManager.getProperty(ROOT_ELEMENT_DECL);
        if (rootDecl == null) {
            fRootElementDeclQName = null;
            fRootElementDeclaration = null;
        } else if (rootDecl instanceof javax.xml.namespace.QName) {
            fRootElementDeclQName = (javax.xml.namespace.QName) rootDecl;
            fRootElementDeclaration = null;
        } else {
            fRootElementDeclaration = (XSElementDecl) rootDecl;
            fRootElementDeclQName = null;
        }
    } catch (XMLConfigurationException e) {
        fRootElementDeclQName = null;
        fRootElementDeclaration = null;
    }
    boolean ignoreXSIType;
    try {
        ignoreXSIType = componentManager.getFeature(IGNORE_XSI_TYPE);
    } catch (XMLConfigurationException e) {
        ignoreXSIType = false;
    }
    // An initial value of -1 means that the root element considers itself  
    // below the depth where xsi:type stopped being ignored (which means that  
    // xsi:type attributes will not be ignored for the entire document)  
    fIgnoreXSITypeDepth = ignoreXSIType ? 0 : -1;
    try {
        fIDCChecking = componentManager.getFeature(IDENTITY_CONSTRAINT_CHECKING);
    } catch (XMLConfigurationException e) {
        fIDCChecking = true;
    }
    try {
        fValidationState.setIdIdrefChecking(componentManager.getFeature(ID_IDREF_CHECKING));
    } catch (XMLConfigurationException e) {
        fValidationState.setIdIdrefChecking(true);
    }
    try {
        fValidationState.setUnparsedEntityChecking(componentManager.getFeature(UNPARSED_ENTITY_CHECKING));
    } catch (XMLConfigurationException e) {
        fValidationState.setUnparsedEntityChecking(true);
    }
    // get schema location properties  
    try {
        fExternalSchemas = (String) componentManager.getProperty(SCHEMA_LOCATION);
        fExternalNoNamespaceSchema = (String) componentManager.getProperty(SCHEMA_NONS_LOCATION);
    } catch (XMLConfigurationException e) {
        fExternalSchemas = null;
        fExternalNoNamespaceSchema = null;
    }
    // store the external schema locations. they are set when reset is called,  
    // so any other schemaLocation declaration for the same namespace will be  
    // effectively ignored. becuase we choose to take first location hint  
    // available for a particular namespace.  
    XMLSchemaLoader.processExternalHints(fExternalSchemas, fExternalNoNamespaceSchema, fLocationPairs, fXSIErrorReporter.fErrorReporter);
    try {
        fJaxpSchemaSource = componentManager.getProperty(JAXP_SCHEMA_SOURCE);
    } catch (XMLConfigurationException e) {
        fJaxpSchemaSource = null;
    }
    // clear grammars, and put the one for schema namespace there  
    try {
        fGrammarPool = (XMLGrammarPool) componentManager.getProperty(XMLGRAMMAR_POOL);
    } catch (XMLConfigurationException e) {
        fGrammarPool = null;
    }
    fState4XsiType.setSymbolTable(symbolTable);
    fState4ApplyDefault.setSymbolTable(symbolTable);
}
