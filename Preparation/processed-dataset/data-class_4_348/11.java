/**
   * Determine if the string-value of a node is whitespace
   *
   * @param nodeHandle The node Handle.
   *
   * @return Return true if the given node is whitespace.
   */
public boolean isWhitespace(int nodeHandle) {
    int type = getNodeType(nodeHandle);
    Node node = getNode(nodeHandle);
    if (TEXT_NODE == type || CDATA_SECTION_NODE == type) {
        // If this is a DTM text node, it may be made of multiple DOM text  
        // nodes -- including navigating into Entity References. DOM2DTM  
        // records the first node in the sequence and requires that we  
        // pick up the others when we retrieve the DTM node's value.  
        //  
        // %REVIEW% DOM Level 3 is expected to add a "whole text"  
        // retrieval method which performs this function for us.  
        FastStringBuffer buf = StringBufferPool.get();
        while (node != null) {
            buf.append(node.getNodeValue());
            node = logicalNextDOMTextNode(node);
        }
        boolean b = buf.isWhitespace(0, buf.length());
        StringBufferPool.free(buf);
        return b;
    }
    return false;
}
