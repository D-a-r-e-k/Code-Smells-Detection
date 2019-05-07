void getBaseTableNames(OrderedHashSet set) {
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
}
