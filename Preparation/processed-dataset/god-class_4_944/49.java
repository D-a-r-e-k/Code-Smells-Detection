//GEN-LAST:event_disconnectMenuItemActionPerformed  
private void addEntityMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_addEntityMenuItemActionPerformed  
    if (!isOfflineMode()) {
        getConManager();
    }
    if (isOfflineMode()) {
        Entity entity = new Entity(root.getRootPackage(), "entity", null);
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeModel.getRoot();
        addObject(parent, entity, true, false);
        tree.updateUI();
        return;
    }
    if (conManager == null) {
        logger.log("Can't add entity - no database connection!");
        return;
    }
    new SelectTablesDialog(this).show();
    new Thread(new Runnable() {

        public void run() {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeModel.getRoot();
            Object referencingModule = tree.getLastSelectedPathComponent();
            String templateValue = (String) root.config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_RELATIONS);
            if ("true".equalsIgnoreCase(templateValue)) {
                relationsEnabled = true;
            } else if ("false".equalsIgnoreCase(templateValue)) {
                relationsEnabled = false;
            } else {
                relationsEnabled = false;
            }
            ArrayList createdEntities = new ArrayList();
            for (Iterator tabIt = SelectTablesDialog.getTablelist().iterator(); tabIt.hasNext(); ) {
                String table = (String) tabIt.next();
                logger.log("Creating entity for table '" + table + "'...");
                ArrayList pKeys = DatabaseUtils.getPrimaryKeys(table);
                String pKey = "";
                if (pKeys.size() == 1) {
                    pKey = (String) pKeys.get(0);
                } else if (pKeys.size() > 1) {
                    String tableClassName = Utils.toClassName(table);
                    pKey = root.getRootPackage() + ".entity" + tableClassName + "PK";
                }
                Entity entity = new Entity(root.getRootPackage(), table, pKey);
                entity.setTableName(table);
                addObject(parent, entity, true, false);
                if (referencingModule instanceof Session) {
                    Session session = (Session) referencingModule;
                    session.addRef(entity.getRefName());
                }
                ArrayList columns = sortColumns(DatabaseUtils.getColumns(table), pKeys, entity, pKey);
                if (relationsEnabled) {
                    generateRelationsFromDB(entity);
                }
                // Now build the fields.  
                for (Iterator colIt = columns.iterator(); colIt.hasNext(); ) {
                    Column column = (Column) colIt.next();
                    Field field = new Field(entity, column);
                    addObject(entity, field, false, false);
                    if (column.getName().equalsIgnoreCase(pKey)) {
                        entity.setPKeyType(field.getType(column));
                    }
                }
                createdEntities.add(entity);
            }
            if (relationsEnabled) {
                checkForAssociationEntities(createdEntities);
            }
            // This will make sure the relations are updated correctly in the gui.  
            for (Iterator iterator = createdEntities.iterator(); iterator.hasNext(); ) {
                Entity entity = (Entity) iterator.next();
                entity.notifyRelationsThatConstructionIsFinished();
            }
            logger.log("...finished!");
            tree.updateUI();
        }
    }).start();
}
