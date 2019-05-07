/**
     * This method notifies of the start of a parameter entity. The parameter
     * entity name start with a '%' character.
     * 
     * @param name     The name of the parameter entity.
     * @param identifier The resource identifier.
     * @param encoding The auto-detected IANA encoding name of the entity
     *                 stream. This value will be null in those situations
     *                 where the entity encoding is not auto-detected (e.g.
     *                 internal parameter entities).
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
    if (fPerformValidation && fDTDGrammar != null && fGrammarBucket.getStandalone()) {
        checkStandaloneEntityRef(name, fDTDGrammar, fEntityDecl, fErrorReporter);
    }
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.startParameterEntity(name, identifier, encoding, augs);
    if (fDTDHandler != null) {
        fDTDHandler.startParameterEntity(name, identifier, encoding, augs);
    }
}
