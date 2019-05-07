// empty()  
/**
     * A start of either a mixed or children content model. A mixed
     * content model will immediately be followed by a call to the
     * <code>pcdata()</code> method. A children content model will
     * contain additional groups and/or elements.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #any
     * @see #empty
     */
public void startGroup(Augmentations augs) throws XNIException {
    fMixed = false;
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.startGroup(augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.startGroup(augs);
    }
}
