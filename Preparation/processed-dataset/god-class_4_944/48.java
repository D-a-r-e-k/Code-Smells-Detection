//GEN-LAST:event_saveButtonActionPerformed  
private void disconnectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_disconnectMenuItemActionPerformed  
    conManager = null;
    databaseConnectionLabel.setText("Database Connection: not connected");
    disconnectMenuItem.setEnabled(false);
    DatabaseUtils.clearCache();
}
