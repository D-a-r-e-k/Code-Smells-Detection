/**
   * Get a Node from an identity index.
   *
   * NEEDSDOC @param nodeIdentity
   *
   * NEEDSDOC ($objectName$) @return
   */
protected Node lookupNode(int nodeIdentity) {
    return (Node) m_nodes.elementAt(nodeIdentity);
}
