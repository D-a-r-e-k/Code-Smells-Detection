/** 
     * Check standalone entity reference. 
     * Made static to make common between the validator and loader.
     * 
     * @param name
     *@param grammar    grammar to which entity belongs
     * @param tempEntityDecl    empty entity declaration to put results in
     * @param errorReporter     error reporter to send errors to
     *
     * @throws XNIException Thrown by application to signal an error.
     */
protected static void checkStandaloneEntityRef(String name, DTDGrammar grammar, XMLEntityDecl tempEntityDecl, XMLErrorReporter errorReporter) throws XNIException {
    // check VC: Standalone Document Declartion, entities references appear in the document.  
    int entIndex = grammar.getEntityDeclIndex(name);
    if (entIndex > -1) {
        grammar.getEntityDecl(entIndex, tempEntityDecl);
        if (tempEntityDecl.inExternal) {
            errorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { name }, XMLErrorReporter.SEVERITY_ERROR);
        }
    }
}
