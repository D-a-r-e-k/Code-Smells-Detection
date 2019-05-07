// childrenElement(String)  
/**
     * The separator between choices or sequences of a mixed or children
     * content model.
     * 
     * @param separator The type of children separator.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #SEPARATOR_CHOICE
     * @see #SEPARATOR_SEQUENCE
     */
public void separator(short separator, Augmentations augs) throws XNIException {
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.separator(separator, augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.separator(separator, augs);
    }
}
