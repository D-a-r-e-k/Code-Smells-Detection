/**
   * Retrieves an attribute node by by qualified name and namespace URI.
   *
   * @param nodeHandle int Handle of the node upon which to look up this attribute..
   * @param namespaceURI The namespace URI of the attribute to
   *   retrieve, or null.
   * @param name The local name of the attribute to
   *   retrieve.
   * @return The attribute node handle with the specified name (
   *   <code>nodeName</code>) or <code>DTM.NULL</code> if there is no such
   *   attribute.
   */
public int getAttributeNode(int nodeHandle, String namespaceURI, String name) {
    // %OPT% This is probably slower than it needs to be.  
    if (null == namespaceURI)
        namespaceURI = "";
    int type = getNodeType(nodeHandle);
    if (DTM.ELEMENT_NODE == type) {
        // Assume that attributes immediately follow the element.  
        int identity = makeNodeIdentity(nodeHandle);
        while (DTM.NULL != (identity = getNextNodeIdentity(identity))) {
            // Assume this can not be null.  
            type = _type(identity);
            // %REVIEW%  
            // Should namespace nodes be retrievable DOM-style as attrs?  
            // If not we need a separate function... which may be desirable  
            // architecturally, but which is ugly from a code point of view.  
            // (If we REALLY insist on it, this code should become a subroutine  
            // of both -- retrieve the node, then test if the type matches  
            // what you're looking for.)  
            if (type == DTM.ATTRIBUTE_NODE || type == DTM.NAMESPACE_NODE) {
                Node node = lookupNode(identity);
                String nodeuri = node.getNamespaceURI();
                if (null == nodeuri)
                    nodeuri = "";
                String nodelocalname = node.getLocalName();
                if (nodeuri.equals(namespaceURI) && name.equals(nodelocalname))
                    return makeNodeHandle(identity);
            } else // if (DTM.NAMESPACE_NODE != type)  
            {
                break;
            }
        }
    }
    return DTM.NULL;
}
