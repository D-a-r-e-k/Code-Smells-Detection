Statement compileAlterSequence() {
    read();
    HsqlName schema = session.getSchemaHsqlName(token.namePrefix);
    NumberSequence sequence = database.schemaManager.getSequence(token.tokenString, schema.name, true);
    read();
    if (token.tokenType == Tokens.RENAME) {
        read();
        readThis(Tokens.TO);
        return compileRenameObject(sequence.getName(), SchemaObject.SEQUENCE);
    }
    NumberSequence copy = sequence.duplicate();
    readSequenceOptions(copy, false, true);
    String sql = getLastPart();
    Object[] args = new Object[] { sequence, copy };
    return new StatementSchema(sql, StatementTypes.ALTER_SEQUENCE, args);
}
