void setUpdatability() {
    if (!isUpdatable) {
        return;
    }
    isUpdatable = false;
    if (!isMergeable) {
        return;
    }
    if (!isTopLevel) {
        return;
    }
    if (isAggregated) {
        return;
    }
    if (sortAndSlice.hasLimit() || sortAndSlice.hasOrder()) {
        return;
    }
    RangeVariable rangeVar = rangeVariables[0];
    Table table = rangeVar.getTable();
    Table baseTable = table.getBaseTable();
    if (baseTable == null) {
        return;
    }
    isInsertable = table.isInsertable();
    isUpdatable = table.isUpdatable();
    if (!isInsertable && !isUpdatable) {
        return;
    }
    IntValueHashMap columns = new IntValueHashMap();
    boolean[] checkList;
    int[] baseColumnMap = table.getBaseTableColumnMap();
    int[] columnMap = new int[indexLimitVisible];
    if (queryCondition != null) {
        tempSet.clear();
        collectSubQueriesAndReferences(tempSet, queryCondition);
        if (tempSet.contains(table.getName()) || tempSet.contains(baseTable.getName())) {
            isUpdatable = false;
            isInsertable = false;
            return;
        }
    }
    for (int i = 0; i < indexLimitVisible; i++) {
        Expression expression = exprColumns[i];
        if (expression.getType() == OpTypes.COLUMN) {
            String name = expression.getColumn().getName().name;
            if (columns.containsKey(name)) {
                columns.put(name, 1);
                continue;
            }
            columns.put(name, 0);
        } else {
            tempSet.clear();
            collectSubQueriesAndReferences(tempSet, expression);
            if (tempSet.contains(table.getName())) {
                isUpdatable = false;
                isInsertable = false;
                return;
            }
        }
    }
    isUpdatable = false;
    for (int i = 0; i < indexLimitVisible; i++) {
        if (accessibleColumns[i]) {
            Expression expression = exprColumns[i];
            if (expression.getType() == OpTypes.COLUMN) {
                String name = expression.getColumn().getName().name;
                if (columns.get(name) == 0) {
                    int index = table.findColumn(name);
                    columnMap[i] = baseColumnMap[index];
                    if (columnMap[i] != -1) {
                        isUpdatable = true;
                    }
                    continue;
                }
            }
        }
        columnMap[i] = -1;
        isInsertable = false;
    }
    if (isInsertable) {
        checkList = baseTable.getColumnCheckList(columnMap);
        for (int i = 0; i < checkList.length; i++) {
            if (checkList[i]) {
                continue;
            }
            ColumnSchema column = baseTable.getColumn(i);
            if (column.isIdentity() || column.isGenerated() || column.hasDefault() || column.isNullable()) {
            } else {
                isInsertable = false;
                break;
            }
        }
    }
    if (!isUpdatable) {
        isInsertable = false;
    }
    if (isUpdatable) {
        this.columnMap = columnMap;
        this.baseTable = baseTable;
        if (view != null) {
            return;
        }
        indexLimitRowId++;
        hasRowID = true;
        if (!baseTable.isFileBased()) {
            indexLimitRowId++;
            hasMemoryRow = true;
        }
        indexLimitData = indexLimitRowId;
    }
}
