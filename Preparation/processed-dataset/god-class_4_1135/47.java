// endGroup()  
/**
     * The end of a content model.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endContentModel(Augmentations augs) throws XNIException {
    // call handlers  
    if (fDTDGrammar != null)
        fDTDGrammar.endContentModel(augs);
    if (fDTDContentModelHandler != null) {
        fDTDContentModelHandler.endContentModel(augs);
    }
}
