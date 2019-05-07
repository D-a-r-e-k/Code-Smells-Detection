/**
     * This method notifies the end of a parameter entity. Parameter entity
     * names begin with a '%' character.
     * 
     * @param name The name of the parameter entity.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endParameterEntity(String name, Augmentations augs) throws XNIException {
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.endParameterEntity(name, augs);
    if (fDTDHandler != null) {
        fDTDHandler.endParameterEntity(name, augs);
    }
}
