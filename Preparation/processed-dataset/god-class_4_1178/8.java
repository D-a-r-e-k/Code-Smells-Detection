/** Get the handle from a Node. This is a more robust version of
   * getHandleFromNode, intended to be usable by the public.
   *
   * <p>%OPT% This will be pretty slow.</p>
   * 
   * %REVIEW% This relies on being able to test node-identity via
   * object-identity. DTM2DOM proxying is a great example of a case where
   * that doesn't work. DOM Level 3 will provide the isSameNode() method
   * to fix that, but until then this is going to be flaky.
   *
   * @param node A node, which may be null.
   *
   * @return The node handle or <code>DTM.NULL</code>.  */
public int getHandleOfNode(Node node) {
    if (null != node) {
        // Is Node actually within the same document? If not, don't search!  
        // This would be easier if m_root was always the Document node, but  
        // we decided to allow wrapping a DTM around a subtree.  
        if ((m_root == node) || (m_root.getNodeType() == DOCUMENT_NODE && m_root == node.getOwnerDocument()) || (m_root.getNodeType() != DOCUMENT_NODE && m_root.getOwnerDocument() == node.getOwnerDocument())) {
            // If node _is_ in m_root's tree, find its handle  
            //  
            // %OPT% This check may be improved significantly when DOM  
            // Level 3 nodeKey and relative-order tests become  
            // available!  
            for (Node cursor = node; cursor != null; cursor = (cursor.getNodeType() != ATTRIBUTE_NODE) ? cursor.getParentNode() : ((org.w3c.dom.Attr) cursor).getOwnerElement()) {
                if (cursor == m_root)
                    // We know this node; find its handle.  
                    return getHandleFromNode(node);
            }
        }
    }
    // if node!=null  
    return DTM.NULL;
}
