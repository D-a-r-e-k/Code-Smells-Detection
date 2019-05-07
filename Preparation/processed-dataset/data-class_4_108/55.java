//GEN-LAST:event_generateJavaApplicationAsMenuItemActionPerformed  
private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_deleteMenuItemActionPerformed  
    TreePath[] sel = tree.getSelectionPaths();
    for (int i = 0; i < sel.length; i++) {
        Object selectedObject = sel[i].getLastPathComponent();
        if (!(selectedObject instanceof Config || selectedObject instanceof App || selectedObject instanceof Paths || selectedObject instanceof Datasource)) {
            treeModel.removeNodeFromParent((DefaultMutableTreeNode) selectedObject);
        }
        if (selectedObject instanceof Entity) {
            TemplateString table = ((Entity) selectedObject).getLocalTableName();
            SelectTablesDialog.getAlreadyselected().remove(table);
            DatabaseUtils.clearColumnsCacheForTable(table.toString());
        }
    }
    setFileNeedsSavingIndicator(true);
}
