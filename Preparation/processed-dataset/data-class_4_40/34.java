// startDocument(XMLLocator,String,Augmentations) 
/** Start prefix mapping. */
public void startPrefixMapping(String prefix, String uri, Augmentations augs) throws XNIException {
    // check for end of document 
    if (fSeenRootElementEnd) {
        return;
    }
    // call handler 
    if (fDocumentHandler != null) {
        XercesBridge.getInstance().XMLDocumentHandler_startPrefixMapping(fDocumentHandler, prefix, uri, augs);
    }
}
