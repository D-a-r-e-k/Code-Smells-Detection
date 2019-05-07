/**
   * Get the handle from a Node.
   * <p>%OPT% This will be pretty slow.</p>
   *
   * <p>%OPT% An XPath-like search (walk up DOM to root, tracking path;
   * walk down DTM reconstructing path) might be considerably faster
   * on later nodes in large documents. That might also imply improving
   * this call to handle nodes which would be in this DTM but
   * have not yet been built, which might or might not be a Good Thing.</p>
   * 
   * %REVIEW% This relies on being able to test node-identity via
   * object-identity. DTM2DOM proxying is a great example of a case where
   * that doesn't work. DOM Level 3 will provide the isSameNode() method
   * to fix that, but until then this is going to be flaky.
   *
   * @param node A node, which may be null.
   *
   * @return The node handle or <code>DTM.NULL</code>.
   */
private int getHandleFromNode(Node node) {
    if (null != node) {
        int len = m_nodes.size();
        boolean isMore;
        int i = 0;
        do {
            for (; i < len; i++) {
                if (m_nodes.elementAt(i) == node)
                    return makeNodeHandle(i);
            }
            isMore = nextNode();
            len = m_nodes.size();
        } while (isMore || i < len);
    }
    return DTM.NULL;
}
