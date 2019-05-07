Statement compileAlterDomain() {
    read();
    HsqlName schema = session.getSchemaHsqlName(token.namePrefix);
    Type domain = database.schemaManager.getDomain(token.tokenString, schema.name, true);
    read();
    switch(token.tokenType) {
        case Tokens.RENAME:
            {
                read();
                readThis(Tokens.TO);
                return compileRenameObject(domain.getName(), SchemaObject.DOMAIN);
            }
        case Tokens.DROP:
            {
                read();
                if (token.tokenType == Tokens.DEFAULT) {
                    read();
                    return compileAlterDomainDropDefault(domain);
                } else if (token.tokenType == Tokens.CONSTRAINT) {
                    read();
                    checkIsSchemaObjectName();
                    HsqlName name = database.schemaManager.getSchemaObjectName(domain.getSchemaName(), token.tokenString, SchemaObject.CONSTRAINT, true);
                    read();
                    return compileAlterDomainDropConstraint(domain, name);
                } else {
                    throw unexpectedToken();
                }
            }
        case Tokens.SET:
            {
                read();
                readThis(Tokens.DEFAULT);
                Expression e = readDefaultClause(domain);
                return compileAlterDomainSetDefault(domain, e);
            }
        case Tokens.ADD:
            {
                read();
                if (token.tokenType == Tokens.CONSTRAINT || token.tokenType == Tokens.CHECK) {
                    HsqlArrayList tempConstraints = new HsqlArrayList();
                    readConstraint(domain, tempConstraints);
                    Constraint c = (Constraint) tempConstraints.get(0);
                    return compileAlterDomainAddConstraint(domain, c);
                }
            }
    }
    throw unexpectedToken();
}
