// startElement(QName,XMLAttributes)  
/**
     * An empty element.
     * 
     * @param element    The name of the element.
     * @param attributes The element attributes.     
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    boolean removed = handleStartElement(element, attributes, augs);
    if (fDocumentHandler != null) {
        fDocumentHandler.emptyElement(element, attributes, augs);
    }
    if (!removed) {
        handleEndElement(element, augs, true);
    }
}
