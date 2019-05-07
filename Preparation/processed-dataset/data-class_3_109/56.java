//GEN-LAST:event_typeTextFocusLost  
private void nameTextFocusLost(java.awt.event.FocusEvent evt) {
    //GEN-FIRST:event_nameTextFocusLost  
    if (!nameText.getText().equals(oldName)) {
        parentEntity.notifyRelationsThatFieldNameChanged(oldName, nameText.getText());
        oldName = nameText.getText();
    }
    JagGenerator.stateChanged(true);
}
