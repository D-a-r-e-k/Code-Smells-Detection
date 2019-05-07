// startCDATA(Augmentations) 
/** End CDATA section. */
public void endCDATA(Augmentations augs) throws XNIException {
    // check for end of document 
    if (fSeenRootElementEnd) {
        return;
    }
    // call handler 
    if (fDocumentHandler != null) {
        fDocumentHandler.endCDATA(augs);
    }
}
