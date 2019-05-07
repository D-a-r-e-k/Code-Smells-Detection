// startElement(QName,XMLAttributes,Augmentations) 
/** Empty element. */
public void emptyElement(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
    startElement(element, attrs, augs);
    endElement(element, augs);
}
