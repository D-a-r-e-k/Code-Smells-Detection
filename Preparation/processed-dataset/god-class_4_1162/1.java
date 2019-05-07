/**
     * Builds the JTree which represents the navigation menu and then returns it
     *
     * @return The navigation tree.
     */
private JTree buildTree() {
    String name = Messages.message("menuBar.colopedia");
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(new ColopediaTreeItem(null, null, name, null));
    new TerrainDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    new ResourcesDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    new GoodsDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    new UnitDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    new BuildingDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    new FatherDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    new NationDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    new NationTypeDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    new ConceptDetailPanel(getFreeColClient(), getGUI(), this).addSubTrees(root);
    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    tree = new JTree(treeModel) {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, super.getPreferredSize().height);
        }
    };
    tree.setRootVisible(false);
    tree.setCellRenderer(new ColopediaTreeCellRenderer());
    tree.setOpaque(false);
    tree.addTreeSelectionListener(this);
    listPanel.add(tree);
    Enumeration allNodes = root.depthFirstEnumeration();
    while (allNodes.hasMoreElements()) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) allNodes.nextElement();
        ColopediaTreeItem item = (ColopediaTreeItem) node.getUserObject();
        nodeMap.put(item.getId(), node);
    }
    return tree;
}
