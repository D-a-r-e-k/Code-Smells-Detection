//GEN-LAST:event_addFieldMenuItemActionPerformed  
private void fieldButtonActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_fieldButtonActionPerformed  
    // TODO add your handling code here:  
    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    if (!(selected instanceof Entity)) {
        JOptionPane.showMessageDialog(this, "A field can only be added to an entity.  Please first select an entity in the application tree.", "Can't add field!", JOptionPane.ERROR_MESSAGE);
        return;
    }
    Entity selectedEntity = (Entity) selected;
    Field newField = new Field(selectedEntity, new Column());
    selectedEntity.add(newField);
    tree.setSelectionPath(new TreePath(newField.getPath()));
    tree.updateUI();
}
