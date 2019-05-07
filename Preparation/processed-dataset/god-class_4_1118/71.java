Statement compileAlterSchemaRename() {
    HsqlName name = readSchemaName();
    checkSchemaUpdateAuthorisation(name);
    readThis(Tokens.RENAME);
    readThis(Tokens.TO);
    HsqlName newName = readNewSchemaName();
    String sql = getLastPart();
    Object[] args = new Object[] { name, newName };
    return new StatementSchema(sql, StatementTypes.RENAME_OBJECT, args);
}
