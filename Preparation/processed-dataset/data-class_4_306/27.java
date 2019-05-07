// endAttlist()  
/**
     * An internal entity declaration.
     * 
     * @param name The name of the entity. Parameter entity names start with
     *             '%', whereas the name of a general entity is just the 
     *             entity name.
     * @param text The value of the entity.
     * @param nonNormalizedText The non-normalized value of the entity. This
     *             value contains the same sequence of characters that was in 
     *             the internal entity declaration, without any entity
     *             references expanded.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs) throws XNIException {
    DTDGrammar grammar = (fDTDGrammar != null ? fDTDGrammar : fGrammarBucket.getActiveGrammar());
    int index = grammar.getEntityDeclIndex(name);
    //If the same entity is declared more than once, the first declaration  
    //encountered is binding, SAX requires only effective(first) declaration  
    //to be reported to the application  
    //REVISIT: Does it make sense to pass duplicate Entity information across  
    //the pipeline -- nb?  
    //its a new entity and hasn't been declared.  
    if (index == -1) {
        //store internal entity declaration in grammar  
        if (fDTDGrammar != null)
            fDTDGrammar.internalEntityDecl(name, text, nonNormalizedText, augs);
        // call handlers  
        if (fDTDHandler != null) {
            fDTDHandler.internalEntityDecl(name, text, nonNormalizedText, augs);
        }
    }
}
