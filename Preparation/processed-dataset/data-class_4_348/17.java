/**
   * Given a node handle, return its DOM-style namespace URI
   * (As defined in Namespaces, this is the declared URI which this node's
   * prefix -- or default in lieu thereof -- was mapped to.)
   *
   * <p>%REVIEW% Null or ""? -sb</p>
   *
   * @param nodeHandle the id of the node.
   * @return String URI value of this node's namespace, or null if no
   * namespace was resolved.
   */
public String getNamespaceURI(int nodeHandle) {
    if (JJK_NEWCODE) {
        int id = makeNodeIdentity(nodeHandle);
        if (id == NULL)
            return null;
        Node node = (Node) m_nodes.elementAt(id);
        return node.getNamespaceURI();
    } else {
        String nsuri;
        short type = getNodeType(nodeHandle);
        switch(type) {
            case DTM.ATTRIBUTE_NODE:
            case DTM.ELEMENT_NODE:
            case DTM.ENTITY_REFERENCE_NODE:
            case DTM.NAMESPACE_NODE:
            case DTM.PROCESSING_INSTRUCTION_NODE:
                {
                    Node node = getNode(nodeHandle);
                    // assume not null.  
                    nsuri = node.getNamespaceURI();
                }
                break;
            default:
                nsuri = null;
        }
        return nsuri;
    }
}
