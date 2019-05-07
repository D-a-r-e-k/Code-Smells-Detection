// emptyElement(QName,XMLAttributes,Augmentations) 
/** Characters. */
public void characters(XMLString text, Augmentations augs) throws XNIException {
    if (fInCDATASection) {
        Node node = fCurrentNode.getLastChild();
        if (node != null && node.getNodeType() == Node.CDATA_SECTION_NODE) {
            CDATASection cdata = (CDATASection) node;
            cdata.appendData(text.toString());
        } else {
            CDATASection cdata = fDocument.createCDATASection(text.toString());
            fCurrentNode.appendChild(cdata);
        }
    } else {
        Node node = fCurrentNode.getLastChild();
        if (node != null && node.getNodeType() == Node.TEXT_NODE) {
            Text textNode = (Text) node;
            textNode.appendData(text.toString());
        } else {
            Text textNode = fDocument.createTextNode(text.toString());
            fCurrentNode.appendChild(textNode);
        }
    }
}
