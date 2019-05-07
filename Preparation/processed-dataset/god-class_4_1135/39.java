// startContentModel(String)  
/** 
     * A content model of ANY. 
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #empty
     * @see #startGroup
     */
public void any(Augmentations augs) throws XNIException {
    if (fDTDGrammar != null)
        fDTDGrammar.any(augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.any(augs);
    }
}
