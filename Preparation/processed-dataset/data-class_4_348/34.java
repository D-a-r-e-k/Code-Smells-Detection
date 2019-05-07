/**
   * Directly call the
   * characters method on the passed ContentHandler for the
   * string-value of the given node (see http://www.w3.org/TR/xpath#data-model
   * for the definition of a node's string-value). Multiple calls to the
   * ContentHandler's characters methods may well occur for a single call to
   * this method.
   *
   * @param nodeHandle The node ID.
   * @param ch A non-null reference to a ContentHandler.
   *
   * @throws org.xml.sax.SAXException
   */
public void dispatchCharactersEvents(int nodeHandle, org.xml.sax.ContentHandler ch, boolean normalize) throws org.xml.sax.SAXException {
    if (normalize) {
        XMLString str = getStringValue(nodeHandle);
        str = str.fixWhiteSpace(true, true, false);
        str.dispatchCharactersEvents(ch);
    } else {
        int type = getNodeType(nodeHandle);
        Node node = getNode(nodeHandle);
        dispatchNodeData(node, ch, 0);
        // Text coalition -- a DTM text node may represent multiple  
        // DOM nodes.  
        if (TEXT_NODE == type || CDATA_SECTION_NODE == type) {
            while (null != (node = logicalNextDOMTextNode(node))) {
                dispatchNodeData(node, ch, 0);
            }
        }
    }
}
