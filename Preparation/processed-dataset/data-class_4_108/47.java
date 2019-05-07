//GEN-LAST:event_importMenuItemActionPerformed  
private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_saveButtonActionPerformed  
    if (file == null) {
        saveAsMenuItemActionPerformed(evt);
    } else {
        save();
    }
}
