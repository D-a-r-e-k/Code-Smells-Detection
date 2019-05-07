// callStartElement(QName,XMLAttributes,Augmentations) 
/** Call document handler end element. */
protected final void callEndElement(QName element, Augmentations augs) throws XNIException {
    fDocumentHandler.endElement(element, augs);
}
