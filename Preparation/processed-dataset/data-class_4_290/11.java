StatementSession compileDeclareLocalTableOrNull() {
    int position = super.getPosition();
    try {
        readThis(Tokens.DECLARE);
        readThis(Tokens.LOCAL);
        readThis(Tokens.TEMPORARY);
        readThis(Tokens.TABLE);
    } catch (Exception e) {
        // may be cursor 
        rewind(position);
        return null;
    }
    if (token.namePrePrefix != null) {
        throw unexpectedToken();
    }
    if (token.namePrePrefix == null && (token.namePrefix == null || Tokens.T_MODULE.equals(token.namePrefix))) {
    } else {
        throw unexpectedToken();
    }
    HsqlName name = readNewSchemaObjectName(SchemaObject.TABLE, false);
    name.schema = SqlInvariants.MODULE_HSQLNAME;
    Table table = TableUtil.newTable(database, TableBase.TEMP_TABLE, name);
    StatementSchema cs = compileCreateTableBody(table);
    HsqlArrayList constraints = (HsqlArrayList) cs.arguments[1];
    for (int i = 0; i < constraints.size(); i++) {
        Constraint c = (Constraint) constraints.get(i);
        if (c.getConstraintType() == SchemaObject.ConstraintTypes.FOREIGN_KEY) {
            throw unexpectedToken(Tokens.T_FOREIGN);
        }
    }
    StatementSession ss = new StatementSession(StatementTypes.DECLARE_SESSION_TABLE, cs.arguments);
    return ss;
}
