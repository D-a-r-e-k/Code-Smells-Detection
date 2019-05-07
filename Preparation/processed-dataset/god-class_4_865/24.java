/** Text declaration. */
public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
    fSeenAnything = true;
    // check for end of document 
    if (fSeenRootElementEnd) {
        return;
    }
    // call handler 
    if (fDocumentHandler != null) {
        fDocumentHandler.textDecl(version, encoding, augs);
    }
}
