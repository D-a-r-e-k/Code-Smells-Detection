/**Creates an individual panel, adds it to the JTree and the list of panels, and returns the tree node.
   * @param t the title of this panel
   * @param parent the parent tree node
   * @return this tree node
   */
private PanelTreeNode _createPanel(String t, PanelTreeNode parent) {
    PanelTreeNode ptNode = new PanelTreeNode(t);
    //parent.add(ptNode); 
    _treeModel.insertNodeInto(ptNode, parent, parent.getChildCount());
    // Make sure tree node is visible 
    TreeNode[] pathArray = ptNode.getPath();
    TreePath path = new TreePath(pathArray);
    //     System.out.println("path has class " + pathArray.getClass()); 
    //     System.out.println("last path compenent has class " + path.getLastPathComponent().getClass()); 
    _tree.expandPath(path);
    return ptNode;
}
