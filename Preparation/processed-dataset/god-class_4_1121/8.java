private void dropRoutine(Session session, HsqlName name, boolean cascade) {
    checkSchemaUpdateAuthorisation(session, name.schema);
    session.database.schemaManager.removeSchemaObject(name, cascade);
}
