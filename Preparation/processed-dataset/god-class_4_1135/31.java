// notationDecl(String,XMLResourceIdentifier, Augmentations)  
/**
     * The start of a conditional section.
     * 
     * @param type The type of the conditional section. This value will
     *             either be CONDITIONAL_INCLUDE or CONDITIONAL_IGNORE.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #CONDITIONAL_INCLUDE
     * @see #CONDITIONAL_IGNORE
     */
public void startConditional(short type, Augmentations augs) throws XNIException {
    // set state  
    fInDTDIgnore = type == XMLDTDHandler.CONDITIONAL_IGNORE;
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.startConditional(type, augs);
    if (fDTDHandler != null) {
        fDTDHandler.startConditional(type, augs);
    }
}
