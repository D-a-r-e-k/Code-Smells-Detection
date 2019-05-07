// startCDATA()  
/**
     * The end of a CDATA section.
     *
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endCDATA(Augmentations augs) throws XNIException {
    // call handlers  
    fInCDATA = false;
    if (fDocumentHandler != null) {
        fDocumentHandler.endCDATA(augs);
    }
}
