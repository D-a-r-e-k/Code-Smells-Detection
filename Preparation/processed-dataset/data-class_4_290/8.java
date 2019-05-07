private Statement compileAlterTableDropConstraint(Table t) {
    boolean cascade = false;
    SchemaObject object = readSchemaObjectName(t.getSchemaName(), SchemaObject.CONSTRAINT);
    if (token.tokenType == Tokens.RESTRICT) {
        read();
    } else if (token.tokenType == Tokens.CASCADE) {
        read();
        cascade = true;
    }
    Object[] args = new Object[] { object.getName(), ValuePool.getInt(SchemaObject.CONSTRAINT), Boolean.valueOf(cascade), Boolean.valueOf(false) };
    String sql = getLastPart();
    Statement cs = new StatementSchema(sql, StatementTypes.DROP_CONSTRAINT, args);
    cs.writeTableNames = getReferenceArray(t.getName(), cascade);
    return cs;
}
