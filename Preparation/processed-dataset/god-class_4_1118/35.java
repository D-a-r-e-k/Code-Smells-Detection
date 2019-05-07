StatementSchema compileCreateSchema() {
    HsqlName schemaName = null;
    String authorisation = null;
    read();
    if (token.tokenType != Tokens.AUTHORIZATION) {
        schemaName = readNewSchemaName();
    }
    if (token.tokenType == Tokens.AUTHORIZATION) {
        read();
        checkIsSimpleName();
        authorisation = token.tokenString;
        read();
        if (schemaName == null) {
            Grantee owner = database.getGranteeManager().get(authorisation);
            if (owner == null) {
                throw Error.error(ErrorCode.X_28501, authorisation);
            }
            schemaName = database.nameManager.newHsqlName(owner.getName().name, isDelimitedIdentifier(), SchemaObject.SCHEMA);
            SqlInvariants.checkSchemaNameNotSystem(token.tokenString);
        }
    }
    if (SqlInvariants.PUBLIC_ROLE_NAME.equals(authorisation)) {
        throw Error.error(ErrorCode.X_28502, authorisation);
    }
    Grantee owner = authorisation == null ? session.getGrantee() : database.getGranteeManager().get(authorisation);
    if (owner == null) {
        throw Error.error(ErrorCode.X_28501, authorisation);
    }
    if (!session.getGrantee().isSchemaCreator()) {
        throw Error.error(ErrorCode.X_0L000, session.getGrantee().getNameString());
    }
    if (database.schemaManager.schemaExists(schemaName.name)) {
        throw Error.error(ErrorCode.X_42504, schemaName.name);
    }
    if (schemaName.name.equals(SqlInvariants.LOBS_SCHEMA)) {
        schemaName = SqlInvariants.LOBS_SCHEMA_HSQLNAME;
        owner = schemaName.owner;
    }
    String sql = getLastPart();
    Object[] args = new Object[] { schemaName, owner };
    HsqlArrayList list = new HsqlArrayList();
    StatementSchema cs = new StatementSchema(sql, StatementTypes.CREATE_SCHEMA, args, null, null);
    cs.setSchemaHsqlName(schemaName);
    list.add(cs);
    getCompiledStatementBody(list);
    StatementSchema[] array = new StatementSchema[list.size()];
    list.toArray(array);
    boolean swapped;
    do {
        swapped = false;
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].order > array[i + 1].order) {
                StatementSchema temp = array[i + 1];
                array[i + 1] = array[i];
                array[i] = temp;
                swapped = true;
            }
        }
    } while (swapped);
    return new StatementSchemaDefinition(array);
}
