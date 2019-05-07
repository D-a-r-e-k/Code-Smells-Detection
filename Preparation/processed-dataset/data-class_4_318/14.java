// doctypeDecl(String,String,String)  
/**
     * The start of an element.
     *
     * @param element    The name of the element.
     * @param attributes The element attributes.
     * @param augs     Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    Augmentations modifiedAugs = handleStartElement(element, attributes, augs);
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.startElement(element, attributes, modifiedAugs);
    }
}
