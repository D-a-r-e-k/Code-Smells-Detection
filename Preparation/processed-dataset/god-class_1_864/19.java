// endPrefixMapping(String,Augmentations) 
/** Start element. */
public void startElement(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
    Element elementNode = fDocument.createElement(element.rawname);
    int count = attrs != null ? attrs.getLength() : 0;
    for (int i = 0; i < count; i++) {
        String aname = attrs.getQName(i);
        String avalue = attrs.getValue(i);
        if (XMLChar.isValidName(aname)) {
            elementNode.setAttribute(aname, avalue);
        }
    }
    fCurrentNode.appendChild(elementNode);
    fCurrentNode = elementNode;
}
