/**
     * Generates relations for a specified entity by reading foreign key info from the database.
     *
     * @param entity the Entity whose relations will be generated.
     */
private boolean generateRelationsFromDB(Entity entity) {
    if (getConManager() == null) {
        logger.log("Can't regenerate relations - no database!");
        return false;
    }
    log.debug("Get the foreign keys for table: " + entity.getTableName());
    List fkeys = DatabaseUtils.getForeignKeys(entity.getTableName());
    HashMap foreignTablesTally = new HashMap();
    Set foreignKeyFieldNames = new HashSet(fkeys.size());
    Iterator i = fkeys.iterator();
    while (i.hasNext()) {
        ForeignKey fk = (ForeignKey) i.next();
        foreignKeyFieldNames.add(Utils.format(fk.getFkColumnName()));
        Relation relation = new Relation(entity, fk);
        String foreignTable = fk.getPkTableName();
        // Default to unidirectional:  
        relation.setBidirectional(false);
        // No support for one-to-one relations yet. So default to many-to-one  
        relation.setTargetMultiple(true);
        /*
            if (relation.getFkField() != null && relation.getFkField().isPrimaryKey()) {
               // In case the local side is a primary key, we have a one to one relation
               relation.setTargetMultiple(false);
            } else {
               // In case the local side isn't a primary key, we asume to have a many to one relation.
               relation.setTargetMultiple(true);
            }
            */
        if (foreignTablesTally.keySet().contains(foreignTable)) {
            int tally = ((Integer) foreignTablesTally.get(foreignTable)).intValue() + 1;
            relation.setName(relation.getName() + tally);
            foreignTablesTally.put(foreignTable, new Integer(tally));
        } else {
            foreignTablesTally.put(foreignTable, new Integer(1));
        }
        addObject(entity, relation, false, true);
    }
    return true;
}
