// getSchemaDocument(String, XMLInputSource, boolean, short, Element): Element  
/**
     * getSchemaDocument method uses SAXInputSource to parse a schema document.
     * @param schemaNamespace
     * @param schemaSource
     * @param mustResolve
     * @param referType
     * @param referElement
     * @return A schema Element.
     */
private Element getSchemaDocument(String schemaNamespace, SAXInputSource schemaSource, boolean mustResolve, short referType, Element referElement) {
    XMLReader parser = schemaSource.getXMLReader();
    InputSource inputSource = schemaSource.getInputSource();
    boolean hasInput = true;
    IOException exception = null;
    Element schemaElement = null;
    try {
        if (inputSource != null && (inputSource.getSystemId() != null || inputSource.getByteStream() != null || inputSource.getCharacterStream() != null)) {
            // check whether the same document has been parsed before.   
            // If so, return the document corresponding to that system id.  
            XSDKey key = null;
            String schemaId = null;
            if (referType != XSDDescription.CONTEXT_PREPARSE) {
                schemaId = XMLEntityManager.expandSystemId(inputSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                key = new XSDKey(schemaId, referType, schemaNamespace);
                if ((schemaElement = (Element) fTraversed.get(key)) != null) {
                    fLastSchemaWasDuplicate = true;
                    return schemaElement;
                }
            }
            boolean namespacePrefixes = false;
            if (parser != null) {
                try {
                    namespacePrefixes = parser.getFeature(NAMESPACE_PREFIXES);
                } catch (SAXException se) {
                }
            } else {
                try {
                    parser = XMLReaderFactory.createXMLReader();
                } catch (SAXException se) {
                    parser = new SAXParser();
                }
                try {
                    parser.setFeature(NAMESPACE_PREFIXES, true);
                    namespacePrefixes = true;
                    // If this is a Xerces SAX parser set the security manager if there is one  
                    if (parser instanceof SAXParser) {
                        Object securityManager = fSchemaParser.getProperty(SECURITY_MANAGER);
                        if (securityManager != null) {
                            parser.setProperty(SECURITY_MANAGER, securityManager);
                        }
                    }
                } catch (SAXException se) {
                }
            }
            // If XML names and Namespace URIs are already internalized we  
            // can avoid running them through the SymbolTable.  
            boolean stringsInternalized = false;
            try {
                stringsInternalized = parser.getFeature(STRING_INTERNING);
            } catch (SAXException exc) {
            }
            if (fXSContentHandler == null) {
                fXSContentHandler = new SchemaContentHandler();
            }
            fXSContentHandler.reset(fSchemaParser, fSymbolTable, namespacePrefixes, stringsInternalized);
            parser.setContentHandler(fXSContentHandler);
            parser.setErrorHandler(fErrorReporter.getSAXErrorHandler());
            parser.parse(inputSource);
            // Disconnect the schema loader and other objects from the XMLReader  
            try {
                parser.setContentHandler(null);
                parser.setErrorHandler(null);
            } catch (Exception e) {
            }
            Document schemaDocument = fXSContentHandler.getDocument();
            schemaElement = schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null;
            return getSchemaDocument0(key, schemaId, schemaElement);
        } else {
            hasInput = false;
        }
    } catch (SAXParseException spe) {
        throw SAX2XNIUtil.createXMLParseException0(spe);
    } catch (SAXException se) {
        throw SAX2XNIUtil.createXNIException0(se);
    } catch (IOException ioe) {
        exception = ioe;
    }
    return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
}
