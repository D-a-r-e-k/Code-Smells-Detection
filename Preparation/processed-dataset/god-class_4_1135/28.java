// internalEntityDecl(String,XMLString,XMLString)  
/**
     * An external entity declaration.
     * 
     * @param name     The name of the entity. Parameter entity names start
     *                 with '%', whereas the name of a general entity is just
     *                 the entity name.
     * @param identifier    An object containing all location information 
     *                      pertinent to this external entity.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
    DTDGrammar grammar = (fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar());
    int index = grammar.getEntityDeclIndex(name);
    //If the same entity is declared more than once, the first declaration  
    //encountered is binding, SAX requires only effective(first) declaration  
    //to be reported to the application  
    //REVISIT: Does it make sense to pass duplicate entity information across  
    //the pipeline -- nb?  
    //its a new entity and hasn't been declared.  
    if (index == -1) {
        //store external entity declaration in grammar  
        if (fDTDGrammar != null)
            fDTDGrammar.externalEntityDecl(name, identifier, augs);
        // call handlers  
        if (fDTDHandler != null) {
            fDTDHandler.externalEntityDecl(name, identifier, augs);
        }
    }
}
