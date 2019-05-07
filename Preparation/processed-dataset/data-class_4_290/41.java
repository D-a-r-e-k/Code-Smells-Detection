Statement compileRenameObject(HsqlName name, int objectType) {
    HsqlName newName = readNewSchemaObjectName(objectType, true);
    String sql = getLastPart();
    Object[] args = new Object[] { name, newName };
    return new StatementSchema(sql, StatementTypes.RENAME_OBJECT, args);
}
