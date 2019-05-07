/**
     * The end of the DTD external subset.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endExternalSubset(Augmentations augs) throws XNIException {
    if (fDTDGrammar != null)
        fDTDGrammar.endExternalSubset(augs);
    if (fDTDHandler != null) {
        fDTDHandler.endExternalSubset(augs);
    }
}
