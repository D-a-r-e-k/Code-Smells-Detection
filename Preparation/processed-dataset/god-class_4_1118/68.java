/**
     * Allows changes to type of column or addition of an IDENTITY generator.
     * IDENTITY is not removed if it does not appear in new column definition
     * Constraint definitions are not allowed
     */
private void processAlterColumnType(Table table, ColumnSchema oldCol, boolean fullDefinition) {
    ColumnSchema newCol;
    if (oldCol.isGenerated()) {
        throw Error.error(ErrorCode.X_42561);
    }
    if (fullDefinition) {
        HsqlArrayList list = new HsqlArrayList();
        Constraint c = table.getPrimaryConstraint();
        if (c == null) {
            c = new Constraint(null, null, SchemaObject.ConstraintTypes.TEMP);
        }
        list.add(c);
        newCol = readColumnDefinitionOrNull(table, oldCol.getName(), list);
        if (newCol == null) {
            throw Error.error(ErrorCode.X_42000);
        }
        if (oldCol.isIdentity() && newCol.isIdentity()) {
            throw Error.error(ErrorCode.X_42525);
        }
        if (list.size() > 1) {
            throw Error.error(ErrorCode.X_42524);
        }
    } else {
        Type type = readTypeDefinition(true);
        if (oldCol.isIdentity()) {
            if (!type.isIntegralType()) {
                throw Error.error(ErrorCode.X_42561);
            }
        }
        newCol = oldCol.duplicate();
        newCol.setType(type);
    }
    TableWorks tw = new TableWorks(session, table);
    tw.retypeColumn(oldCol, newCol);
}
