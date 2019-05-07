// endCDATA()  
/**
     * The end of the document.
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endDocument(Augmentations augs) throws XNIException {
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.endDocument(augs);
    }
}
