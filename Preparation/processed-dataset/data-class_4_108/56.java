//GEN-LAST:event_deleteMenuItemActionPerformed  
private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_newMenuItemActionPerformed  
    root = new Root();
    file = null;
    fileNameLabel.setText("Application file:");
    fileNameLabel.setToolTipText("No application file selected");
    disconnectMenuItemActionPerformed(null);
    treeModel.setRoot(root);
}
