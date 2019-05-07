/** Creates an individual panel, adds it to the JTree and the list of panels, and returns the tree node. Adds to the root node.
   *  @param t the title of this panel
   *  @return this tree node
   */
private PanelTreeNode _createPanel(String t) {
    return _createPanel(t, _rootNode);
}
