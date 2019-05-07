void processAlterDomain() {
    HsqlName schema = session.getSchemaHsqlName(token.namePrefix);
    checkSchemaUpdateAuthorisation(schema);
    Type domain = database.schemaManager.getDomain(token.tokenString, schema.name, true);
    read();
    switch(token.tokenType) {
        case Tokens.RENAME:
            {
                read();
                readThis(Tokens.TO);
                HsqlName newName = readNewSchemaObjectName(SchemaObject.DOMAIN, true);
                newName.setSchemaIfNull(schema);
                if (domain.getSchemaName() != newName.schema) {
                    throw Error.error(ErrorCode.X_42505, newName.schema.name);
                }
                checkSchemaUpdateAuthorisation(schema);
                database.schemaManager.renameSchemaObject(domain.getName(), newName);
                return;
            }
        case Tokens.DROP:
            {
                read();
                if (token.tokenType == Tokens.DEFAULT) {
                    read();
                    domain.userTypeModifier.removeDefaultClause();
                    return;
                } else if (token.tokenType == Tokens.CONSTRAINT) {
                    read();
                    checkIsSchemaObjectName();
                    HsqlName name = database.schemaManager.getSchemaObjectName(domain.getSchemaName(), token.tokenString, SchemaObject.CONSTRAINT, true);
                    read();
                    database.schemaManager.removeSchemaObject(name);
                    return;
                } else {
                    throw unexpectedToken();
                }
            }
        case Tokens.SET:
            {
                read();
                readThis(Tokens.DEFAULT);
                Expression e = readDefaultClause(domain);
                domain.userTypeModifier.setDefaultClause(e);
                return;
            }
        case Tokens.ADD:
            {
                read();
                if (token.tokenType == Tokens.CONSTRAINT || token.tokenType == Tokens.CHECK) {
                    HsqlArrayList tempConstraints = new HsqlArrayList();
                    readConstraint(domain, tempConstraints);
                    Constraint c = (Constraint) tempConstraints.get(0);
                    domain.userTypeModifier.addConstraint(c);
                    database.schemaManager.addSchemaObject(c);
                    return;
                }
            }
    }
    throw unexpectedToken();
}
