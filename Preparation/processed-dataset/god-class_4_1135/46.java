// occurrence(short)  
/**
     * The end of a group for mixed or children content models.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endGroup(Augmentations augs) throws XNIException {
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.endGroup(augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.endGroup(augs);
    }
}
