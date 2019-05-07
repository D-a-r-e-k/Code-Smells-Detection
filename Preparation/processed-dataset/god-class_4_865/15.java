/** Comment. */
public void comment(XMLString text, Augmentations augs) throws XNIException {
    fSeenAnything = true;
    consumeEarlyTextIfNeeded();
    if (fDocumentHandler != null) {
        fDocumentHandler.comment(text, augs);
    }
}
