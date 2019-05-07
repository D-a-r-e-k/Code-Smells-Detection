/**
   * Given a node handle, return its XPath-style localname.
   * (As defined in Namespaces, this is the portion of the name after any
   * colon character).
   *
   * @param nodeHandle the id of the node.
   * @return String Local name of this node.
   */
public String getLocalName(int nodeHandle) {
    if (JJK_NEWCODE) {
        int id = makeNodeIdentity(nodeHandle);
        if (NULL == id)
            return null;
        Node newnode = (Node) m_nodes.elementAt(id);
        String newname = newnode.getLocalName();
        if (null == newname) {
            // XSLT treats PIs, and possibly other things, as having QNames.  
            String qname = newnode.getNodeName();
            if ('#' == qname.charAt(0)) {
                //  Match old default for this function  
                // This conversion may or may not be necessary  
                newname = "";
            } else {
                int index = qname.indexOf(':');
                newname = (index < 0) ? qname : qname.substring(index + 1);
            }
        }
        return newname;
    } else {
        String name;
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
                    name = node.getLocalName();
                    if (null == name) {
                        String qname = node.getNodeName();
                        int index = qname.indexOf(':');
                        name = (index < 0) ? qname : qname.substring(index + 1);
                    }
                }
                break;
            default:
                name = "";
        }
        return name;
    }
}
