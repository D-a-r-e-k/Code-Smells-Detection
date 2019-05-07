// startDocument(XMLLocator,String,Augmentations) 
// old methods 
/** XML declaration. */
public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
    if (!fSeenAnything && fDocumentHandler != null) {
        fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
    }
}
