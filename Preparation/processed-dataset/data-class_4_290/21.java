StatementSchema compileCreateSequence() {
    read();
    /*
                CREATE SEQUENCE <name>
                [AS {INTEGER | BIGINT}]
                [START WITH <value>]
                [INCREMENT BY <value>]
        */
    HsqlName name = readNewSchemaObjectName(SchemaObject.SEQUENCE, false);
    NumberSequence sequence = new NumberSequence(name, Type.SQL_INTEGER);
    readSequenceOptions(sequence, true, false);
    String sql = getLastPart();
    Object[] args = new Object[] { sequence };
    return new StatementSchema(sql, StatementTypes.CREATE_SEQUENCE, args);
}
