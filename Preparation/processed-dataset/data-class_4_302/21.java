/**
     * Starts an entity.
     * <p>
     * This method can be used to insert an application defined XML
     * entity stream into the parsing stream.
     *
     * @param name           The name of the entity.
     * @param xmlInputSource The input source of the entity.
     * @param literal        True if this entity is started within a
     *                       literal value.
     * @param isExternal    whether this entity should be treated as an internal or external entity.
     *
     * @throws IOException  Thrown on i/o error.
     * @throws XNIException Thrown by entity handler to signal an error.
     */
public void startEntity(String name, XMLInputSource xmlInputSource, boolean literal, boolean isExternal) throws IOException, XNIException {
    String encoding = setupCurrentEntity(name, xmlInputSource, literal, isExternal);
    //when entity expansion limit is set by the Application, we need to  
    //check for the entity expansion limit set by the parser, if number of entity  
    //expansions exceeds the entity expansion limit, parser will throw fatal error.  
    // Note that this is intentionally unbalanced; it counts  
    // the number of expansions *per document*.  
    if (fSecurityManager != null && fEntityExpansionCount++ > fEntityExpansionLimit) {
        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "EntityExpansionLimitExceeded", new Object[] { new Integer(fEntityExpansionLimit) }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
        // is there anything better to do than reset the counter?  
        // at least one can envision debugging applications where this might  
        // be useful...  
        fEntityExpansionCount = 0;
    }
    // call handler  
    if (fEntityHandler != null) {
        fEntityHandler.startEntity(name, fResourceIdentifier, encoding, null);
    }
}
