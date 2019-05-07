// textDecl(String,String,Augmentations) 
/** End entity. */
public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
    // check for end of document 
    if (fSeenRootElementEnd) {
        return;
    }
    // call handler 
    if (fDocumentHandler != null) {
        fDocumentHandler.endGeneralEntity(name, augs);
    }
}
