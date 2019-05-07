// unparsedEntityDecl(String,XMLResourceIdentifier,String,Augmentations)  
/**
     * A notation declaration
     * 
     * @param name     The name of the notation.
     * @param identifier    An object containing all location information 
     *                      pertinent to this notation.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
    // VC: Unique Notation Name  
    if (fValidation) {
        DTDGrammar grammar = (fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar());
        if (grammar.getNotationDeclIndex(name) != -1) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "UniqueNotationName", new Object[] { name }, XMLErrorReporter.SEVERITY_ERROR);
        }
    }
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.notationDecl(name, identifier, augs);
    if (fDTDHandler != null) {
        fDTDHandler.notationDecl(name, identifier, augs);
    }
}
