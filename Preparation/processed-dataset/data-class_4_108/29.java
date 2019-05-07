/**
     * Sometimes a relation specifies an entity that hasn't been added to a session bean (all related entities
     * must be represented in the session fa?ade).  This method adds those entities automatically.
     *
     * @return <code>false</code> if a related entity couldn't be imported into the application.
     */
private boolean addRelatedEntitiesToSessionBeans() {
    boolean somethingAdded = false;
    //1: create a map of entity name --> set of related foreign tables, and  
    //            map of local table --> entity bean name, and  
    //            map of entity bean name --> local table  
    HashMap relatedTablesPerEB = new HashMap();
    HashMap entityPerTable = new HashMap();
    HashMap tablePerEntity = new HashMap();
    Iterator entities = root.getEntityEjbs().iterator();
    while (entities.hasNext()) {
        Entity entity = (Entity) entities.next();
        entityPerTable.put(entity.getLocalTableName().toString(), entity.getRefName());
        tablePerEntity.put(entity.getRefName(), entity.getLocalTableName().toString());
        for (int i = 0; i < entity.getChildCount(); i++) {
            TreeNode child = entity.getChildAt(i);
            if (child instanceof Relation) {
                Relation relation = (Relation) child;
                String relatedTableName = relation.getForeignTable();
                Set existing = (Set) relatedTablesPerEB.get(entity.getRefName());
                if (existing == null) {
                    existing = new HashSet();
                }
                existing.add(relatedTableName);
                relatedTablesPerEB.put(entity.getRefName(), existing);
            }
        }
    }
    //2: remove all related foreign tables from the map, where the table is the local table of an entity that already  
    //   appears within a session bean.  
    Iterator sessions = root.getSessionEjbs().iterator();
    while (sessions.hasNext()) {
        Session session = (Session) sessions.next();
        Iterator entitiesWithinSession = session.getEntityRefs().iterator();
        while (entitiesWithinSession.hasNext()) {
            String localTable = (String) tablePerEntity.get(entitiesWithinSession.next());
            Iterator relatedEntitySets = relatedTablesPerEB.values().iterator();
            while (relatedEntitySets.hasNext()) {
                Set set = (Set) relatedEntitySets.next();
                set.remove(localTable);
            }
        }
    }
    //3: for each session bean, add any related entities not already contained within a session bean.  
    HashSet addedTables = new HashSet();
    sessions = root.getSessionEjbs().iterator();
    while (sessions.hasNext()) {
        Session session = (Session) sessions.next();
        Iterator entitiesWithinSession = session.getEntityRefs().iterator();
        while (entitiesWithinSession.hasNext()) {
            String entityName = (String) entitiesWithinSession.next();
            Set tablesToBeAdded = (Set) relatedTablesPerEB.get(entityName);
            if (tablesToBeAdded != null) {
                Iterator i = tablesToBeAdded.iterator();
                while (i.hasNext()) {
                    String table = (String) i.next();
                    if (!addedTables.contains(table)) {
                        String entity = (String) entityPerTable.get(table);
                        if (entity == null) {
                            JOptionPane.showMessageDialog(this, "Entity '" + entityName + "' contains a relation to a table '" + table + "'\n" + "for which no entity bean exists in the current application.\n" + "Please either create a new entity bean for this table, or delete the relation.", "Invalid Container-managed relation!", JOptionPane.ERROR_MESSAGE);
                            return false;
                        } else {
                            session.addRelationRef(entity);
                            addedTables.add(table);
                            somethingAdded = true;
                            JOptionPane.showMessageDialog(this, "Entity '" + entityName + "' added to the service bean '" + session.getRefName() + "' contains a relation to the entity '" + entity + "', which doesn't appear in any service beans.\n" + "The relation requires accessor methods to '" + entity + "', so these were automatically added to the service bean.", "Service bean modified", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        }
    }
    //recursively call this method if an entity had to be added, because possible that entity also contains  
    //relation references to other entities that need to be imported into a session bean...  
    if (somethingAdded) {
        addRelatedEntitiesToSessionBeans();
    }
    return true;
}
