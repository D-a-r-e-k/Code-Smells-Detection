// startConditional(short)  
/**
     * The end of a conditional section.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endConditional(Augmentations augs) throws XNIException {
    // set state  
    fInDTDIgnore = false;
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.endConditional(augs);
    if (fDTDHandler != null) {
        fDTDHandler.endConditional(augs);
    }
}
