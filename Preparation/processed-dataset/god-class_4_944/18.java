/**
     * When the table name associated with an entity has been updated, calling this method
     * updates the cache.
     *
     * @param entityName
     * @param newTableName
     */
public static void entityHasupdatedTableName(String entityName, String newTableName) {
    synchronized (entitiesByTableName) {
        Iterator i = entitiesByTableName.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            Entity entity = (Entity) entry.getValue();
            if (entity.getName().equals(entityName)) {
                entitiesByTableName.remove(entry.getKey());
                entitiesByTableName.put(newTableName, entity);
            }
        }
    }
}
