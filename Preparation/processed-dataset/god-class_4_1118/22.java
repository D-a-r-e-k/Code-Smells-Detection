StatementSchema compileCreateDomain() {
    UserTypeModifier userTypeModifier = null;
    HsqlName name;
    read();
    name = readNewSchemaObjectName(SchemaObject.DOMAIN, false);
    readIfThis(Tokens.AS);
    Type type = readTypeDefinition(false).duplicate();
    Expression defaultClause = null;
    if (readIfThis(Tokens.DEFAULT)) {
        defaultClause = readDefaultClause(type);
    }
    userTypeModifier = new UserTypeModifier(name, SchemaObject.DOMAIN, type);
    userTypeModifier.setDefaultClause(defaultClause);
    type.userTypeModifier = userTypeModifier;
    HsqlArrayList tempConstraints = new HsqlArrayList();
    compileContext.currentDomain = type;
    while (true) {
        boolean end = false;
        switch(token.tokenType) {
            case Tokens.CONSTRAINT:
            case Tokens.CHECK:
                readConstraint(type, tempConstraints);
                break;
            default:
                end = true;
                break;
        }
        if (end) {
            break;
        }
    }
    compileContext.currentDomain = null;
    for (int i = 0; i < tempConstraints.size(); i++) {
        Constraint c = (Constraint) tempConstraints.get(i);
        c.prepareCheckConstraint(session, null, false);
        userTypeModifier.addConstraint(c);
    }
    String sql = getLastPart();
    Object[] args = new Object[] { type };
    return new StatementSchema(sql, StatementTypes.CREATE_DOMAIN, args);
}
