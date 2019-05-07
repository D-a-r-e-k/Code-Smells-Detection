// getSchemaDocument(String, DOMInputSource, boolean, short, Element): Element  
/**
     * getSchemaDocument method uses StAXInputSource to parse a schema document.
     * @param schemaNamespace
     * @param schemaSource
     * @param mustResolve
     * @param referType
     * @param referElement
     * @return A schema Element.
     */
private Element getSchemaDocument(String schemaNamespace, StAXInputSource schemaSource, boolean mustResolve, short referType, Element referElement) {
    IOException exception = null;
    Element schemaElement = null;
    try {
        final boolean consumeRemainingContent = schemaSource.shouldConsumeRemainingContent();
        final XMLStreamReader streamReader = schemaSource.getXMLStreamReader();
        final XMLEventReader eventReader = schemaSource.getXMLEventReader();
        // check whether the same document has been parsed before.   
        // If so, return the document corresponding to that system id.  
        XSDKey key = null;
        String schemaId = null;
        if (referType != XSDDescription.CONTEXT_PREPARSE) {
            schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
            boolean isDocument = consumeRemainingContent;
            if (!isDocument) {
                if (streamReader != null) {
                    isDocument = (streamReader.getEventType() == XMLStreamReader.START_DOCUMENT);
                } else {
                    isDocument = eventReader.peek().isStartDocument();
                }
            }
            if (isDocument) {
                key = new XSDKey(schemaId, referType, schemaNamespace);
                if ((schemaElement = (Element) fTraversed.get(key)) != null) {
                    fLastSchemaWasDuplicate = true;
                    return schemaElement;
                }
            }
        }
        if (fStAXSchemaParser == null) {
            fStAXSchemaParser = new StAXSchemaParser();
        }
        fStAXSchemaParser.reset(fSchemaParser, fSymbolTable);
        if (streamReader != null) {
            fStAXSchemaParser.parse(streamReader);
            if (consumeRemainingContent) {
                while (streamReader.hasNext()) {
                    streamReader.next();
                }
            }
        } else {
            fStAXSchemaParser.parse(eventReader);
            if (consumeRemainingContent) {
                while (eventReader.hasNext()) {
                    eventReader.nextEvent();
                }
            }
        }
        Document schemaDocument = fStAXSchemaParser.getDocument();
        schemaElement = schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null;
        return getSchemaDocument0(key, schemaId, schemaElement);
    } catch (XMLStreamException e) {
        StAXLocationWrapper slw = new StAXLocationWrapper();
        slw.setLocation(e.getLocation());
        throw new XMLParseException(slw, e.getMessage(), e);
    } catch (IOException e) {
        exception = e;
    }
    return getSchemaDocument1(mustResolve, true, schemaSource, referElement, exception);
}
