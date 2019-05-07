/**
     * Reads column constraints
     */
void readColumnConstraints(Table table, ColumnSchema column, HsqlArrayList constraintList) {
    boolean end = false;
    boolean hasNotNullConstraint = false;
    boolean hasNullNoiseWord = false;
    boolean hasPrimaryKey = false;
    while (true) {
        HsqlName constName = null;
        if (token.tokenType == Tokens.CONSTRAINT) {
            read();
            constName = readNewDependentSchemaObjectName(table.getName(), SchemaObject.CONSTRAINT);
        }
        switch(token.tokenType) {
            case Tokens.PRIMARY:
                {
                    if (hasNullNoiseWord || hasPrimaryKey) {
                        throw unexpectedToken();
                    }
                    read();
                    readThis(Tokens.KEY);
                    Constraint existingConst = (Constraint) constraintList.get(0);
                    if (existingConst.constType == SchemaObject.ConstraintTypes.PRIMARY_KEY) {
                        throw Error.error(ErrorCode.X_42532);
                    }
                    OrderedHashSet set = new OrderedHashSet();
                    set.add(column.getName().name);
                    if (constName == null) {
                        constName = database.nameManager.newAutoName("PK", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
                    }
                    Constraint c = new Constraint(constName, set, SchemaObject.ConstraintTypes.PRIMARY_KEY);
                    constraintList.set(0, c);
                    column.setPrimaryKey(true);
                    hasPrimaryKey = true;
                    break;
                }
            case Tokens.UNIQUE:
                {
                    read();
                    OrderedHashSet set = new OrderedHashSet();
                    set.add(column.getName().name);
                    if (constName == null) {
                        constName = database.nameManager.newAutoName("CT", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
                    }
                    Constraint c = new Constraint(constName, set, SchemaObject.ConstraintTypes.UNIQUE);
                    constraintList.add(c);
                    break;
                }
            case Tokens.FOREIGN:
                {
                    read();
                    readThis(Tokens.KEY);
                }
            // fall through 
            case Tokens.REFERENCES:
                {
                    OrderedHashSet set = new OrderedHashSet();
                    set.add(column.getName().name);
                    Constraint c = readFKReferences(table, constName, set);
                    constraintList.add(c);
                    break;
                }
            case Tokens.CHECK:
                {
                    read();
                    if (constName == null) {
                        constName = database.nameManager.newAutoName("CT", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
                    }
                    Constraint c = new Constraint(constName, null, SchemaObject.ConstraintTypes.CHECK);
                    readCheckConstraintCondition(c);
                    OrderedHashSet set = c.getCheckColumnExpressions();
                    for (int i = 0; i < set.size(); i++) {
                        ExpressionColumn e = (ExpressionColumn) set.get(i);
                        if (column.getName().name.equals(e.getColumnName())) {
                            if (e.getSchemaName() != null && e.getSchemaName() != table.getSchemaName().name) {
                                throw Error.error(ErrorCode.X_42505);
                            }
                        } else {
                            throw Error.error(ErrorCode.X_42501);
                        }
                    }
                    constraintList.add(c);
                    break;
                }
            case Tokens.NOT:
                {
                    if (hasNotNullConstraint || hasNullNoiseWord) {
                        throw unexpectedToken();
                    }
                    read();
                    readThis(Tokens.NULL);
                    if (constName == null) {
                        constName = database.nameManager.newAutoName("CT", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
                    }
                    Constraint c = new Constraint(constName, null, SchemaObject.ConstraintTypes.CHECK);
                    c.check = new ExpressionLogical(column);
                    constraintList.add(c);
                    hasNotNullConstraint = true;
                    break;
                }
            case Tokens.NULL:
                {
                    if (hasNotNullConstraint || hasNullNoiseWord || hasPrimaryKey) {
                        throw unexpectedToken();
                    }
                    if (constName != null) {
                        throw unexpectedToken();
                    }
                    read();
                    hasNullNoiseWord = true;
                    break;
                }
            default:
                end = true;
                break;
        }
        if (end) {
            break;
        }
    }
}
