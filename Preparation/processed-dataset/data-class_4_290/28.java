/**
     * Responsible for handling the creation of table columns during the process
     * of executing CREATE TABLE or ADD COLUMN etc. statements.
     *
     * @param table this table
     * @param hsqlName column name
     * @param constraintList list of constraints
     * @return a Column object with indicated attributes
     */
ColumnSchema readColumnDefinitionOrNull(Table table, HsqlName hsqlName, HsqlArrayList constraintList) {
    boolean isGenerated = false;
    boolean isIdentity = false;
    boolean isPKIdentity = false;
    boolean generatedAlways = false;
    Expression generateExpr = null;
    boolean isNullable = true;
    Expression defaultExpr = null;
    Type typeObject = null;
    NumberSequence sequence = null;
    if (token.tokenType == Tokens.GENERATED) {
        read();
        readThis(Tokens.ALWAYS);
        isGenerated = true;
        generatedAlways = true;
        // not yet 
        throw unexpectedToken(Tokens.T_GENERATED);
    } else if (token.tokenType == Tokens.IDENTITY) {
        read();
        isIdentity = true;
        isPKIdentity = true;
        typeObject = Type.SQL_INTEGER;
        sequence = new NumberSequence(null, 0, 1, typeObject);
    } else if (token.tokenType == Tokens.COMMA) {
        return null;
    } else if (token.tokenType == Tokens.CLOSEBRACKET) {
        return null;
    } else {
        typeObject = readTypeDefinition(true);
    }
    if (isGenerated || isIdentity) {
    } else if (token.tokenType == Tokens.DEFAULT) {
        read();
        defaultExpr = readDefaultClause(typeObject);
    } else if (token.tokenType == Tokens.GENERATED && !isIdentity) {
        read();
        if (token.tokenType == Tokens.BY) {
            read();
            readThis(Tokens.DEFAULT);
        } else {
            readThis(Tokens.ALWAYS);
            generatedAlways = true;
        }
        readThis(Tokens.AS);
        if (token.tokenType == Tokens.IDENTITY) {
            read();
            sequence = new NumberSequence(null, typeObject);
            sequence.setAlways(generatedAlways);
            if (token.tokenType == Tokens.OPENBRACKET) {
                read();
                readSequenceOptions(sequence, false, false);
                readThis(Tokens.CLOSEBRACKET);
            }
            isIdentity = true;
        } else if (token.tokenType == Tokens.OPENBRACKET) {
            if (!generatedAlways) {
                throw super.unexpectedTokenRequire(Tokens.T_ALWAYS);
            }
            isGenerated = true;
        }
    } else if (token.tokenType == Tokens.IDENTITY && !isIdentity) {
        read();
        isIdentity = true;
        isPKIdentity = true;
        sequence = new NumberSequence(null, 0, 1, typeObject);
    }
    if (isGenerated) {
        readThis(Tokens.OPENBRACKET);
        generateExpr = XreadValueExpression();
        readThis(Tokens.CLOSEBRACKET);
    }
    ColumnSchema column = new ColumnSchema(hsqlName, typeObject, isNullable, false, defaultExpr);
    column.setGeneratingExpression(generateExpr);
    readColumnConstraints(table, column, constraintList);
    if (token.tokenType == Tokens.IDENTITY && !isIdentity) {
        read();
        isIdentity = true;
        isPKIdentity = true;
        sequence = new NumberSequence(null, 0, 1, typeObject);
    }
    if (isIdentity) {
        column.setIdentity(sequence);
    }
    if (isPKIdentity && !column.isPrimaryKey()) {
        OrderedHashSet set = new OrderedHashSet();
        set.add(column.getName().name);
        HsqlName constName = database.nameManager.newAutoName("PK", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
        Constraint c = new Constraint(constName, set, SchemaObject.ConstraintTypes.PRIMARY_KEY);
        constraintList.set(0, c);
        column.setPrimaryKey(true);
    }
    return column;
}
