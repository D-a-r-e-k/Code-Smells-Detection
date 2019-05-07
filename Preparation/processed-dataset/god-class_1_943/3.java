//GEN-END:initComponents  
/** Cancels the action and closes the dialog
    * @param evt that triggered the action
    */
private void canelButtonActionPerformed(java.awt.event.ActionEvent evt) //GEN-FIRST:event_canelButtonActionPerformed  
{
    //GEN-HEADEREND:event_canelButtonActionPerformed  
    if (worker != null) {
        worker.interrupt();
    } else {
        setVisible(false);
        dispose();
    }
}
