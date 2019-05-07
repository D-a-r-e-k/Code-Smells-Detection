// endGeneralEntity(String,Augmentations) 
/** Start CDATA section. */
public void startCDATA(Augmentations augs) throws XNIException {
    fSeenAnything = true;
    consumeEarlyTextIfNeeded();
    // check for end of document 
    if (fSeenRootElementEnd) {
        return;
    }
    // call handler 
    if (fDocumentHandler != null) {
        fDocumentHandler.startCDATA(augs);
    }
}
