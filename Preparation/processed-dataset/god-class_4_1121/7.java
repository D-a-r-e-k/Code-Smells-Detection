private void dropSchema(Session session, HsqlName name, boolean cascade) {
    HsqlName schema = session.database.schemaManager.getUserSchemaHsqlName(name.name);
    checkSchemaUpdateAuthorisation(session, schema);
    session.database.schemaManager.dropSchema(session, name.name, cascade);
}
