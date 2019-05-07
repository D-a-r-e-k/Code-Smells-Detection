/**
     * Reads and adds a table constraint definition to the list
     *
     * @param schemaObject table or domain
     * @param constraintList list of constraints
     */
private void readConstraint(SchemaObject schemaObject, HsqlArrayList constraintList) {
    HsqlName constName = null;
    if (token.tokenType == Tokens.CONSTRAINT) {
        read();
        constName = readNewDependentSchemaObjectName(schemaObject.getName(), SchemaObject.CONSTRAINT);
    }
    switch(token.tokenType) {
        case Tokens.PRIMARY:
            {
                if (schemaObject.getName().type != SchemaObject.TABLE) {
                    throw this.unexpectedTokenRequire(Tokens.T_CHECK);
                }
                read();
                readThis(Tokens.KEY);
                Constraint mainConst;
                mainConst = (Constraint) constraintList.get(0);
                if (mainConst.constType == SchemaObject.ConstraintTypes.PRIMARY_KEY) {
                    throw Error.error(ErrorCode.X_42532);
                }
                if (constName == null) {
                    constName = database.nameManager.newAutoName("PK", schemaObject.getSchemaName(), schemaObject.getName(), SchemaObject.CONSTRAINT);
                }
                OrderedHashSet set = readColumnNames(false);
                Constraint c = new Constraint(constName, set, SchemaObject.ConstraintTypes.PRIMARY_KEY);
                constraintList.set(0, c);
                break;
            }
        case Tokens.UNIQUE:
            {
                if (schemaObject.getName().type != SchemaObject.TABLE) {
                    throw this.unexpectedTokenRequire(Tokens.T_CHECK);
                }
                read();
                OrderedHashSet set = readColumnNames(false);
                if (constName == null) {
                    constName = database.nameManager.newAutoName("CT", schemaObject.getSchemaName(), schemaObject.getName(), SchemaObject.CONSTRAINT);
                }
                Constraint c = new Constraint(constName, set, SchemaObject.ConstraintTypes.UNIQUE);
                constraintList.add(c);
                break;
            }
        case Tokens.FOREIGN:
            {
                if (schemaObject.getName().type != SchemaObject.TABLE) {
                    throw this.unexpectedTokenRequire(Tokens.T_CHECK);
                }
                read();
                readThis(Tokens.KEY);
                OrderedHashSet set = readColumnNames(false);
                Constraint c = readFKReferences((Table) schemaObject, constName, set);
                constraintList.add(c);
                break;
            }
        case Tokens.CHECK:
            {
                read();
                if (constName == null) {
                    constName = database.nameManager.newAutoName("CT", schemaObject.getSchemaName(), schemaObject.getName(), SchemaObject.CONSTRAINT);
                }
                Constraint c = new Constraint(constName, null, SchemaObject.ConstraintTypes.CHECK);
                readCheckConstraintCondition(c);
                constraintList.add(c);
                break;
            }
        default:
            {
                if (constName != null) {
                    throw super.unexpectedToken();
                }
            }
    }
}
