// ignorableWhitespace(XMLString)  
/**
     * The end of an element.
     * 
     * @param element The name of the element.
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void endElement(QName element, Augmentations augs) throws XNIException {
    handleEndElement(element, augs, false);
}
