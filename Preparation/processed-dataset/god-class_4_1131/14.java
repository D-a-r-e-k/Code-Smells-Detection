/**
     * Checks whether an entity given by name is declared.
     *
     * @param entityName The name of the entity to check.
     * @return True if the entity is declared, false otherwise.
     */
public boolean isDeclaredEntity(String entityName) {
    Entity entity = (Entity) fEntities.get(entityName);
    return entity != null;
}
