/**
     * Responsible for handling tail of ALTER TABLE ... RENAME ...
     * @param table table
     */
void processAlterTableRename(Table table) {
    HsqlName name = readNewSchemaObjectName(SchemaObject.TABLE, true);
    name.setSchemaIfNull(table.getSchemaName());
    if (table.getSchemaName() != name.schema) {
        throw Error.error(ErrorCode.X_42505);
    }
    database.schemaManager.renameSchemaObject(table.getName(), name);
}
