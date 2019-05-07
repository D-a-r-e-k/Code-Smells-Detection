/** Utility function: Given a DOM Text node, determine whether it is
   * logically followed by another Text or CDATASection node. This may
   * involve traversing into Entity References.
   * 
   * %REVIEW% DOM Level 3 is expected to add functionality which may 
   * allow us to retire this.
   */
private Node logicalNextDOMTextNode(Node n) {
    Node p = n.getNextSibling();
    if (p == null) {
        // Walk out of any EntityReferenceNodes that ended with text  
        for (n = n.getParentNode(); n != null && ENTITY_REFERENCE_NODE == n.getNodeType(); n = n.getParentNode()) {
            p = n.getNextSibling();
            if (p != null)
                break;
        }
    }
    n = p;
    while (n != null && ENTITY_REFERENCE_NODE == n.getNodeType()) {
        // Walk into any EntityReferenceNodes that start with text  
        if (n.hasChildNodes())
            n = n.getFirstChild();
        else
            n = n.getNextSibling();
    }
    if (n != null) {
        // Found a logical next sibling. Is it text?  
        int ntype = n.getNodeType();
        if (TEXT_NODE != ntype && CDATA_SECTION_NODE != ntype)
            n = null;
    }
    return n;
}
