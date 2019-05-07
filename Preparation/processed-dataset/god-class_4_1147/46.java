//storeLocations  
//this is the function where logic of retrieving grammar is written , parser first tries to get the grammar from  
//the local pool, if not in local pool, it gives chance to application to be able to retrieve the grammar, then it  
//tries to parse the grammar using location hints from the give namespace.  
SchemaGrammar findSchemaGrammar(short contextType, String namespace, QName enclosingElement, QName triggeringComponent, XMLAttributes attributes) {
    SchemaGrammar grammar = null;
    //get the grammar from local pool...  
    grammar = fGrammarBucket.getGrammar(namespace);
    if (grammar == null) {
        fXSDDescription.setNamespace(namespace);
        if (fGrammarPool != null) {
            grammar = (SchemaGrammar) fGrammarPool.retrieveGrammar(fXSDDescription);
            if (grammar != null) {
                // put this grammar into the bucket, along with grammars  
                // imported by it (directly or indirectly)  
                if (!fGrammarBucket.putGrammar(grammar, true, fNamespaceGrowth)) {
                    // REVISIT: a conflict between new grammar(s) and grammars  
                    // in the bucket. What to do? A warning? An exception?  
                    fXSIErrorReporter.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "GrammarConflict", null, XMLErrorReporter.SEVERITY_WARNING);
                    grammar = null;
                }
            }
        }
    }
    if (!fUseGrammarPoolOnly && (grammar == null || (fNamespaceGrowth && !hasSchemaComponent(grammar, contextType, triggeringComponent)))) {
        fXSDDescription.reset();
        fXSDDescription.fContextType = contextType;
        fXSDDescription.setNamespace(namespace);
        fXSDDescription.fEnclosedElementName = enclosingElement;
        fXSDDescription.fTriggeringComponent = triggeringComponent;
        fXSDDescription.fAttributes = attributes;
        if (fLocator != null) {
            fXSDDescription.setBaseSystemId(fLocator.getExpandedSystemId());
        }
        Hashtable locationPairs = fLocationPairs;
        Object locationArray = locationPairs.get(namespace == null ? XMLSymbols.EMPTY_STRING : namespace);
        if (locationArray != null) {
            String[] temp = ((XMLSchemaLoader.LocationArray) locationArray).getLocationArray();
            if (temp.length != 0) {
                setLocationHints(fXSDDescription, temp, grammar);
            }
        }
        if (grammar == null || fXSDDescription.fLocationHints != null) {
            boolean toParseSchema = true;
            if (grammar != null) {
                // use location hints instead  
                locationPairs = EMPTY_TABLE;
            }
            // try to parse the grammar using location hints from that namespace..  
            try {
                XMLInputSource xis = XMLSchemaLoader.resolveDocument(fXSDDescription, locationPairs, fEntityResolver);
                if (grammar != null && fNamespaceGrowth) {
                    try {
                        // if we are dealing with a different schema location, then include the new schema  
                        // into the existing grammar  
                        if (grammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(xis.getSystemId(), xis.getBaseSystemId(), false))) {
                            toParseSchema = false;
                        }
                    } catch (MalformedURIException e) {
                    }
                }
                if (toParseSchema) {
                    grammar = fSchemaLoader.loadSchema(fXSDDescription, xis, fLocationPairs);
                }
            } catch (IOException ex) {
                final String[] locationHints = fXSDDescription.getLocationHints();
                fXSIErrorReporter.fErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "schema_reference.4", new Object[] { locationHints != null ? locationHints[0] : XMLSymbols.EMPTY_STRING }, XMLErrorReporter.SEVERITY_WARNING, ex);
            }
        }
    }
    return grammar;
}
