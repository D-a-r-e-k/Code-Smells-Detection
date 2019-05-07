/** Processing instruction. */
public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
    fSeenAnything = true;
    consumeEarlyTextIfNeeded();
    if (fDocumentHandler != null) {
        fDocumentHandler.processingInstruction(target, data, augs);
    }
}
