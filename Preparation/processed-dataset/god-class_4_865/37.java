// getElement(String):HTMLElements.Element 
/** Call document handler start element. */
protected final void callStartElement(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
    fDocumentHandler.startElement(element, attrs, augs);
}
