private Statement compileAlterTableDropPrimaryKey(Table t) {
    boolean cascade = false;
    if (token.tokenType == Tokens.RESTRICT) {
        read();
    } else if (token.tokenType == Tokens.CASCADE) {
        read();
        cascade = true;
    }
    if (!t.hasPrimaryKey()) {
        throw Error.error(ErrorCode.X_42501);
    }
    SchemaObject object = t.getPrimaryConstraint();
    Object[] args = new Object[] { object.getName(), ValuePool.getInt(SchemaObject.CONSTRAINT), Boolean.valueOf(cascade), Boolean.valueOf(false) };
    String sql = getLastPart();
    Statement cs = new StatementSchema(sql, StatementTypes.DROP_CONSTRAINT, args);
    cs.writeTableNames = getReferenceArray(t.getName(), cascade);
    return cs;
}
