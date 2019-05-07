private void dropTable(Session session, HsqlName name, boolean cascade) {
    Table table = session.database.schemaManager.findUserTable(session, name.name, name.schema.name);
    session.database.schemaManager.dropTableOrView(session, table, cascade);
}
