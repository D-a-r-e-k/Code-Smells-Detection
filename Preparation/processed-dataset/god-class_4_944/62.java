//GEN-LAST:event_saveAsMenuItemActionPerformed  
private void connectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_connectMenuItemActionPerformed  
    GenericJdbcManager previous = conManager;
    conManager = null;
    getConManager();
    if (conManager == null) {
        conManager = previous;
    } else {
        //we're connected!  
        DatabaseUtils.clearCache();
        Iterator entities = root.getEntityEjbs().iterator();
        while (entities.hasNext()) {
            Entity entity = (Entity) entities.next();
            for (int i = 0; i < entity.getChildCount(); i++) {
                TreeNode child = entity.getChildAt(i);
                if (child instanceof Relation) {
                    ((RelationPanel) ((Relation) child).getPanel()).initValues(false);
                }
            }
        }
    }
}
