// any()  
/**
     * A content model of EMPTY.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #any
     * @see #startGroup
     */
public void empty(Augmentations augs) throws XNIException {
    if (fDTDGrammar != null)
        fDTDGrammar.empty(augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.empty(augs);
    }
}
