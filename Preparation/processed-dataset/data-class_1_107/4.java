//GEN-LAST:event_canelButtonActionPerformed  
/** Cancels the action and closes the dialog
    * @param evt that triggered the action
    */
private void closeDialog(java.awt.event.WindowEvent evt) {
    //GEN-FIRST:event_closeDialog  
    if (worker != null) {
        worker.interrupt();
    } else {
        setVisible(false);
        dispose();
    }
}
