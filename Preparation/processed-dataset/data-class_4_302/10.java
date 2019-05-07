// addExternalEntity(String,String,String,String)  
/**
     * Checks whether an entity given by name is external.
     *
     * @param entityName The name of the entity to check.
     * @return True if the entity is external, false otherwise
     * (including when the entity is not declared).
     */
public boolean isExternalEntity(String entityName) {
    Entity entity = (Entity) fEntities.get(entityName);
    if (entity == null) {
        return false;
    }
    return entity.isExternal();
}
