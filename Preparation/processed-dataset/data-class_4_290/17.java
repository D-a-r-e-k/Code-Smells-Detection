static void addForeignKey(Session session, Table table, Constraint c, HsqlArrayList constraintList) {
    HsqlName mainTableName = c.getMainTableName();
    if (mainTableName == table.getName()) {
        c.core.mainTable = table;
    } else {
        Table mainTable = session.database.schemaManager.findUserTable(session, mainTableName.name, mainTableName.schema.name);
        if (mainTable == null) {
            if (constraintList == null) {
                throw Error.error(ErrorCode.X_42501, mainTableName.name);
            }
            constraintList.add(c);
            return;
        }
        c.core.mainTable = mainTable;
    }
    c.setColumnsIndexes(table);
    Constraint uniqueConstraint = c.core.mainTable.getUniqueConstraintForColumns(c.core.mainCols, c.core.refCols);
    if (uniqueConstraint == null) {
        throw Error.error(ErrorCode.X_42523);
    }
    Index mainIndex = uniqueConstraint.getMainIndex();
    TableWorks tableWorks = new TableWorks(session, table);
    tableWorks.checkCreateForeignKey(c);
    boolean isForward = c.core.mainTable.getSchemaName() != table.getSchemaName();
    int offset = session.database.schemaManager.getTableIndex(table);
    if (offset != -1 && offset < session.database.schemaManager.getTableIndex(c.core.mainTable)) {
        isForward = true;
    }
    HsqlName refIndexName = session.database.nameManager.newAutoName("IDX", table.getSchemaName(), table.getName(), SchemaObject.INDEX);
    Index index = table.createAndAddIndexStructure(session, refIndexName, c.core.refCols, null, null, false, true, isForward);
    HsqlName mainName = session.database.nameManager.newAutoName("REF", c.getName().name, table.getSchemaName(), table.getName(), SchemaObject.INDEX);
    c.core.uniqueName = uniqueConstraint.getName();
    c.core.mainName = mainName;
    c.core.mainIndex = mainIndex;
    c.core.refTable = table;
    c.core.refName = c.getName();
    c.core.refIndex = index;
    c.isForward = isForward;
    table.addConstraint(c);
    c.core.mainTable.addConstraint(new Constraint(mainName, c));
    session.database.schemaManager.addSchemaObject(c);
}
