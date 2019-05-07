//GEN-LAST:event_foreignKeyCheckBoxActionPerformed  
private void primaryKeyCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_primaryKeyCheckBoxActionPerformed  
    boolean checked = primaryKeyCheckBox.isSelected();
    setPrimaryKey(checked);
    //tell the parent entity bean that its primary key status has changed..  
    if (checked) {
        parentEntity.setPrimaryKey(this);
    } else {
        parentEntity.unsetPrimaryKey(this);
    }
    JagGenerator.stateChanged(false);
    regenerateButton.setEnabled(true);
}
