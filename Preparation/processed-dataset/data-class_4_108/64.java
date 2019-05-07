//GEN-LAST:event_addSessionMenuItemActionPerformed  
private void treeValueChanged(TreeSelectionEvent evt) {
    //GEN-FIRST:event_treeValueChanged  
    TreePath path = evt.getNewLeadSelectionPath();
    JagBean jagBean;
    if (path != null) {
        jagBean = (JagBean) path.getLastPathComponent();
    } else {
        jagBean = (JagBean) treeModel.getRoot();
    }
    splitPane.setRightComponent(jagBean.getPanel());
    splitPane.setDividerLocation(SPLIT_PANE_WIDTH);
}
