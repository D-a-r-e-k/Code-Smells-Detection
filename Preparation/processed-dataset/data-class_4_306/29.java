// externalEntityDecl(String,XMLResourceIdentifier, Augmentations)  
/**
     * An unparsed entity declaration.
     * 
     * @param name     The name of the entity.
     * @param identifier    An object containing all location information 
     *                      pertinent to this entity.
     * @param notation The name of the notation.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs) throws XNIException {
    // VC: Notation declared,  in the production of NDataDecl  
    if (fValidation) {
        fNDataDeclNotations.put(name, notation);
    }
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.unparsedEntityDecl(name, identifier, notation, augs);
    if (fDTDHandler != null) {
        fDTDHandler.unparsedEntityDecl(name, identifier, notation, augs);
    }
}
