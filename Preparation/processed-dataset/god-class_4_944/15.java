public static void addEntity(String refName, Entity entity) {
    entities.put(refName, entity);
    entitiesByTableName.put(entity.getTableName(), entity);
}
