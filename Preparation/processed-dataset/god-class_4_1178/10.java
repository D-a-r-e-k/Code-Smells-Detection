/**
   * Get the string-value of a node as a String object
   * (see http://www.w3.org/TR/xpath#data-model
   * for the definition of a node's string-value).
   *
   * @param nodeHandle The node ID.
   *
   * @return A string object that represents the string-value of the given node.
   */
public XMLString getStringValue(int nodeHandle) {
    int type = getNodeType(nodeHandle);
    Node node = getNode(nodeHandle);
    // %TBD% If an element only has one text node, we should just use it   
    // directly.  
    if (DTM.ELEMENT_NODE == type || DTM.DOCUMENT_NODE == type || DTM.DOCUMENT_FRAGMENT_NODE == type) {
        FastStringBuffer buf = StringBufferPool.get();
        String s;
        try {
            getNodeData(node, buf);
            s = (buf.length() > 0) ? buf.toString() : "";
        } finally {
            StringBufferPool.free(buf);
        }
        return m_xstrf.newstr(s);
    } else if (TEXT_NODE == type || CDATA_SECTION_NODE == type) {
        // If this is a DTM text node, it may be made of multiple DOM text  
        // nodes -- including navigating into Entity References. DOM2DTM  
        // records the first node in the sequence and requires that we  
        // pick up the others when we retrieve the DTM node's value.  
        //  
        // %REVIEW% DOM Level 3 is expected to add a "whole text"  
        // retrieval method which performs this function for us.  
        FastStringBuffer buf = StringBufferPool.get();
        while (node != null) {
            buf.append(node.getNodeValue());
            node = logicalNextDOMTextNode(node);
        }
        String s = (buf.length() > 0) ? buf.toString() : "";
        StringBufferPool.free(buf);
        return m_xstrf.newstr(s);
    } else
        return m_xstrf.newstr(node.getNodeValue());
}
