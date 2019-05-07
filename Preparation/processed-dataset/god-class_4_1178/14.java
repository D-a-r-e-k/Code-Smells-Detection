/**
   * Given a node handle, return the XPath node name.  This should be
   * the name as described by the XPath data model, NOT the DOM-style
   * name.
   *
   * @param nodeHandle the id of the node.
   * @return String Name of this node, which may be an empty string.
   */
public String getNodeNameX(int nodeHandle) {
    String name;
    short type = getNodeType(nodeHandle);
    switch(type) {
        case DTM.NAMESPACE_NODE:
            {
                Node node = getNode(nodeHandle);
                // assume not null.  
                name = node.getNodeName();
                if (name.startsWith("xmlns:")) {
                    name = QName.getLocalPart(name);
                } else if (name.equals("xmlns")) {
                    name = "";
                }
            }
            break;
        case DTM.ATTRIBUTE_NODE:
        case DTM.ELEMENT_NODE:
        case DTM.ENTITY_REFERENCE_NODE:
        case DTM.PROCESSING_INSTRUCTION_NODE:
            {
                Node node = getNode(nodeHandle);
                // assume not null.  
                name = node.getNodeName();
            }
            break;
        default:
            name = "";
    }
    return name;
}
