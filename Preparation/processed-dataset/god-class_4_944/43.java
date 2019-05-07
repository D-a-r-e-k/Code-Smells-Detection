//GEN-LAST:event_editMenuActionPerformed  
private void businessMethodButtonActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_businessMethodButtonActionPerformed  
    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    if (!(selected instanceof Session)) {
        //see if the parent is an Entity..  
        TreePath selectedPath = tree.getSelectionPath();
        selected = (DefaultMutableTreeNode) selectedPath.getParentPath().getLastPathComponent();
        if (!(selected instanceof Session)) {
            JOptionPane.showMessageDialog(this, "A business method can only be added to a service bean.  Please first select a session in the application tree.", "Can't add business method!", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    Session selectedSession = (Session) selected;
    BusinessMethod newBusinessMethod = new BusinessMethod(selectedSession);
    selectedSession.add(newBusinessMethod);
    tree.setSelectionPath(new TreePath(newBusinessMethod.getPath()));
    tree.updateUI();
}
