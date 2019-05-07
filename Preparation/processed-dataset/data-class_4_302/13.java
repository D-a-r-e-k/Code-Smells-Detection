// addUnparsedEntity(String,String,String,String)  
/**
     * Checks whether an entity given by name is unparsed.
     *
     * @param entityName The name of the entity to check.
     * @return True if the entity is unparsed, false otherwise
     *          (including when the entity is not declared).
     */
public boolean isUnparsedEntity(String entityName) {
    Entity entity = (Entity) fEntities.get(entityName);
    if (entity == null) {
        return false;
    }
    return entity.isUnparsed();
}
