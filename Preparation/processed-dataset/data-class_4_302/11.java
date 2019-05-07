/**
     * Checks whether the declaration of an entity given by name is 
     // in the external subset. 
     *
     * @param entityName The name of the entity to check.
     * @return True if the entity was declared in the external subset, false otherwise
     *           (including when the entity is not declared).
     */
public boolean isEntityDeclInExternalSubset(String entityName) {
    Entity entity = (Entity) fEntities.get(entityName);
    if (entity == null) {
        return false;
    }
    return entity.isEntityDeclInExternalSubset();
}
