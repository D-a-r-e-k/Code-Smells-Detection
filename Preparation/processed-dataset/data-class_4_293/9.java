private void dropObject(Session session, HsqlName name, boolean cascade) {
    name = session.database.schemaManager.getSchemaObjectName(name.schema, name.name, name.type, true);
    session.database.schemaManager.removeSchemaObject(name, cascade);
}
