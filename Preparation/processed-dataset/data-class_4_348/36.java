/**
   * Directly create SAX parser events from a subtree.
   *
   * @param nodeHandle The node ID.
   * @param ch A non-null reference to a ContentHandler.
   *
   * @throws org.xml.sax.SAXException
   */
public void dispatchToEvents(int nodeHandle, org.xml.sax.ContentHandler ch) throws org.xml.sax.SAXException {
    TreeWalker treeWalker = m_walker;
    ContentHandler prevCH = treeWalker.getContentHandler();
    if (null != prevCH) {
        treeWalker = new TreeWalker(null);
    }
    treeWalker.setContentHandler(ch);
    try {
        Node node = getNode(nodeHandle);
        treeWalker.traverseFragment(node);
    } finally {
        treeWalker.setContentHandler(null);
    }
}
