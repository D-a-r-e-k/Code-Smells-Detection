/**
   * Given a node handle, return its DOM-style node name. This will
   * include names such as #text or #document.
   *
   * @param nodeHandle the id of the node.
   * @return String Name of this node, which may be an empty string.
   * %REVIEW% Document when empty string is possible...
   * %REVIEW-COMMENT% It should never be empty, should it?
   */
public String getNodeName(int nodeHandle) {
    Node node = getNode(nodeHandle);
    // Assume non-null.  
    return node.getNodeName();
}
