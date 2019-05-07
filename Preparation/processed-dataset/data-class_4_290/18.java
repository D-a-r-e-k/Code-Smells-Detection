private Constraint readFKReferences(Table refTable, HsqlName constraintName, OrderedHashSet refColSet) {
    HsqlName mainTableName;
    OrderedHashSet mainColSet = null;
    readThis(Tokens.REFERENCES);
    HsqlName schema;
    if (token.namePrefix == null) {
        schema = refTable.getSchemaName();
    } else {
        schema = database.schemaManager.getSchemaHsqlName(token.namePrefix);
    }
    if (refTable.getSchemaName() == schema && refTable.getName().name.equals(token.tokenString)) {
        mainTableName = refTable.getName();
        read();
    } else {
        mainTableName = readFKTableName(schema);
    }
    if (token.tokenType == Tokens.OPENBRACKET) {
        mainColSet = readColumnNames(false);
    } else {
        // columns are resolved in the calling method 
        if (mainTableName == refTable.getName()) {
        } else {
        }
    }
    int matchType = OpTypes.MATCH_SIMPLE;
    if (token.tokenType == Tokens.MATCH) {
        read();
        switch(token.tokenType) {
            case Tokens.SIMPLE:
                read();
                break;
            case Tokens.PARTIAL:
                throw super.unsupportedFeature();
            case Tokens.FULL:
                read();
                matchType = OpTypes.MATCH_FULL;
                break;
            default:
                throw unexpectedToken();
        }
    }
    // -- In a while loop we parse a maximium of two 
    // -- "ON" statements following the foreign key 
    // -- definition this can be 
    // -- ON [UPDATE|DELETE] [NO ACTION|RESTRICT|CASCADE|SET [NULL|DEFAULT]] 
    int deleteAction = SchemaObject.ReferentialAction.NO_ACTION;
    int updateAction = SchemaObject.ReferentialAction.NO_ACTION;
    OrderedIntHashSet set = new OrderedIntHashSet();
    while (token.tokenType == Tokens.ON) {
        read();
        if (!set.add(token.tokenType)) {
            throw unexpectedToken();
        }
        if (token.tokenType == Tokens.DELETE) {
            read();
            if (token.tokenType == Tokens.SET) {
                read();
                switch(token.tokenType) {
                    case Tokens.DEFAULT:
                        {
                            read();
                            deleteAction = SchemaObject.ReferentialAction.SET_DEFAULT;
                            break;
                        }
                    case Tokens.NULL:
                        read();
                        deleteAction = SchemaObject.ReferentialAction.SET_NULL;
                        break;
                    default:
                        throw unexpectedToken();
                }
            } else if (token.tokenType == Tokens.CASCADE) {
                read();
                deleteAction = SchemaObject.ReferentialAction.CASCADE;
            } else if (token.tokenType == Tokens.RESTRICT) {
                read();
            } else {
                readThis(Tokens.NO);
                readThis(Tokens.ACTION);
            }
        } else if (token.tokenType == Tokens.UPDATE) {
            read();
            if (token.tokenType == Tokens.SET) {
                read();
                switch(token.tokenType) {
                    case Tokens.DEFAULT:
                        {
                            read();
                            deleteAction = SchemaObject.ReferentialAction.SET_DEFAULT;
                            break;
                        }
                    case Tokens.NULL:
                        read();
                        deleteAction = SchemaObject.ReferentialAction.SET_NULL;
                        break;
                    default:
                        throw unexpectedToken();
                }
            } else if (token.tokenType == Tokens.CASCADE) {
                read();
                updateAction = SchemaObject.ReferentialAction.CASCADE;
            } else if (token.tokenType == Tokens.RESTRICT) {
                read();
            } else {
                readThis(Tokens.NO);
                readThis(Tokens.ACTION);
            }
        } else {
            throw unexpectedToken();
        }
    }
    if (constraintName == null) {
        constraintName = database.nameManager.newAutoName("FK", refTable.getSchemaName(), refTable.getName(), SchemaObject.CONSTRAINT);
    }
    return new Constraint(constraintName, refTable.getName(), refColSet, mainTableName, mainColSet, SchemaObject.ConstraintTypes.FOREIGN_KEY, deleteAction, updateAction, matchType);
}
