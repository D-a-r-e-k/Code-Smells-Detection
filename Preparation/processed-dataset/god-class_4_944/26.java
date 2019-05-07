private void newRelationMenuItemActionPerformed() {
    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    if (!(selected instanceof Entity)) {
        //see if the parent is an Entity..  
        TreePath selectedPath = tree.getSelectionPath();
        selected = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();
        if (!(selected instanceof Entity)) {
            JOptionPane.showMessageDialog(this, "A relation can only be added to an entity bean.  Please first select an entity in the application tree.", "Can't add relation!", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    Entity selectedEntity = (Entity) selected;
    DefaultMutableTreeNode newNode = new Relation(selectedEntity);
    selectedEntity.addRelation((Relation) newNode);
    tree.setSelectionPath(new TreePath(newNode.getPath()));
    tree.updateUI();
}
