/**
   * Return an DOM node for the given node.
   *
   * @param nodeHandle The node ID.
   *
   * @return A node representation of the DTM node.
   */
public Node getNode(int nodeHandle) {
    int identity = makeNodeIdentity(nodeHandle);
    return (Node) m_nodes.elementAt(identity);
}
