// getDocumentSource():XMLDocumentSource 
// removed since Xerces-J 2.3.0 
/** Start document. */
public void startDocument(XMLLocator locator, String encoding, Augmentations augs) throws XNIException {
    startDocument(locator, encoding, null, augs);
}
