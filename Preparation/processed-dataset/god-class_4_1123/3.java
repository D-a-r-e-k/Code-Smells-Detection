// this fk references -> other  :  other read lock 
void collectTableNamesForRead(OrderedHashSet set) {
    if (baseTable.isView()) {
        getTriggerTableNames(set, false);
    } else if (!baseTable.isTemp()) {
        for (int i = 0; i < baseTable.fkConstraints.length; i++) {
            Constraint constraint = baseTable.fkConstraints[i];
            if (type == StatementTypes.UPDATE_WHERE || type == StatementTypes.MERGE) {
                if (ArrayUtil.haveCommonElement(constraint.getRefColumns(), updateColumnMap)) {
                    set.add(baseTable.fkConstraints[i].getMain().getName());
                }
            } else if (type == StatementTypes.INSERT) {
                set.add(baseTable.fkConstraints[i].getMain().getName());
            }
        }
        if (type == StatementTypes.UPDATE_WHERE || type == StatementTypes.MERGE) {
            baseTable.collectFKReadLocks(updateColumnMap, set);
        } else if (type == StatementTypes.DELETE_WHERE) {
            baseTable.collectFKReadLocks(null, set);
        }
        getTriggerTableNames(set, false);
    }
    for (int i = 0; i < rangeVariables.length; i++) {
        Table rangeTable = rangeVariables[i].rangeTable;
        HsqlName name = rangeTable.getName();
        if (rangeTable.isReadOnly() || rangeTable.isTemp()) {
            continue;
        }
        if (name.schema == SqlInvariants.SYSTEM_SCHEMA_HSQLNAME) {
            continue;
        }
        set.add(name);
    }
    for (int i = 0; i < subqueries.length; i++) {
        if (subqueries[i].queryExpression != null) {
            subqueries[i].queryExpression.getBaseTableNames(set);
        }
    }
    for (int i = 0; i < routines.length; i++) {
        set.addAll(routines[i].getTableNamesForRead());
    }
}
