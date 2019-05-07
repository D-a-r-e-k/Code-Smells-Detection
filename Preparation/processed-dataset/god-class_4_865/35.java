// startPrefixMapping(String,String,Augmentations) 
/** End prefix mapping. */
public void endPrefixMapping(String prefix, Augmentations augs) throws XNIException {
    // check for end of document 
    if (fSeenRootElementEnd) {
        return;
    }
    // call handler 
    if (fDocumentHandler != null) {
        XercesBridge.getInstance().XMLDocumentHandler_endPrefixMapping(fDocumentHandler, prefix, augs);
    }
}
