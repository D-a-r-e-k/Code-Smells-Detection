/**
   * Given a namespace handle, return the prefix that the namespace decl is
   * mapping.
   * Given a node handle, return the prefix used to map to the namespace.
   *
   * <p> %REVIEW% Are you sure you want "" for no prefix?  </p>
   * <p> %REVIEW-COMMENT% I think so... not totally sure. -sb  </p>
   *
   * @param nodeHandle the id of the node.
   * @return String prefix of this node's name, or "" if no explicit
   * namespace prefix was given.
   */
public String getPrefix(int nodeHandle) {
    String prefix;
    short type = getNodeType(nodeHandle);
    switch(type) {
        case DTM.NAMESPACE_NODE:
            {
                Node node = getNode(nodeHandle);
                // assume not null.  
                String qname = node.getNodeName();
                int index = qname.indexOf(':');
                prefix = (index < 0) ? "" : qname.substring(index + 1);
            }
            break;
        case DTM.ATTRIBUTE_NODE:
        case DTM.ELEMENT_NODE:
            {
                Node node = getNode(nodeHandle);
                // assume not null.  
                String qname = node.getNodeName();
                int index = qname.indexOf(':');
                prefix = (index < 0) ? "" : qname.substring(0, index);
            }
            break;
        default:
            prefix = "";
    }
    return prefix;
}
