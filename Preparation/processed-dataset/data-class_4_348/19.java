/**
   * Given a node handle, return its node value. This is mostly
   * as defined by the DOM, but may ignore some conveniences.
   * <p>
   *
   * @param nodeHandle The node id.
   * @return String Value of this node, or null if not
   * meaningful for this node type.
   */
public String getNodeValue(int nodeHandle) {
    // The _type(nodeHandle) call was taking the lion's share of our  
    // time, and was wrong anyway since it wasn't coverting handle to  
    // identity. Inlined it.  
    int type = _exptype(makeNodeIdentity(nodeHandle));
    type = (NULL != type) ? getNodeType(nodeHandle) : NULL;
    if (TEXT_NODE != type && CDATA_SECTION_NODE != type)
        return getNode(nodeHandle).getNodeValue();
    // If this is a DTM text node, it may be made of multiple DOM text  
    // nodes -- including navigating into Entity References. DOM2DTM  
    // records the first node in the sequence and requires that we  
    // pick up the others when we retrieve the DTM node's value.  
    //  
    // %REVIEW% DOM Level 3 is expected to add a "whole text"  
    // retrieval method which performs this function for us.  
    Node node = getNode(nodeHandle);
    Node n = logicalNextDOMTextNode(node);
    if (n == null)
        return node.getNodeValue();
    FastStringBuffer buf = StringBufferPool.get();
    buf.append(node.getNodeValue());
    while (n != null) {
        buf.append(n.getNodeValue());
        n = logicalNextDOMTextNode(n);
    }
    String s = (buf.length() > 0) ? buf.toString() : "";
    StringBufferPool.free(buf);
    return s;
}
