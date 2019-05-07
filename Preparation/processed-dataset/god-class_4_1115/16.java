public Expression getEquiJoinExpressions(OrderedHashSet nameSet, RangeVariable rightRange, boolean fullList) {
    HashSet set = new HashSet();
    Expression result = null;
    OrderedHashSet joinColumnNames = new OrderedHashSet();
    for (int i = 0; i < rangeVariableList.size(); i++) {
        RangeVariable range = (RangeVariable) rangeVariableList.get(i);
        HashMappedList columnList = range.rangeTable.columnList;
        for (int j = 0; j < columnList.size(); j++) {
            ColumnSchema column = (ColumnSchema) columnList.get(j);
            String name = range.getColumnAlias(j);
            boolean columnInList = nameSet.contains(name);
            boolean namedJoin = range.namedJoinColumns != null && range.namedJoinColumns.contains(name);
            boolean repeated = !namedJoin && !set.add(name);
            if (repeated && (!fullList || columnInList)) {
                throw Error.error(ErrorCode.X_42578, name);
            }
            if (!columnInList) {
                continue;
            }
            joinColumnNames.add(name);
            int leftPosition = range.rangeTable.getColumnIndex(column.getNameString());
            int rightPosition = rightRange.rangeTable.getColumnIndex(name);
            Expression e = new ExpressionLogical(range, leftPosition, rightRange, rightPosition);
            result = ExpressionLogical.andExpressions(result, e);
            ExpressionColumn col = range.getColumnExpression(name);
            if (col == null) {
                col = new ExpressionColumn(new Expression[] { e.getLeftNode(), e.getRightNode() }, name);
                range.addNamedJoinColumnExpression(name, col);
            } else {
                col.nodes = (Expression[]) ArrayUtil.resizeArray(col.nodes, col.nodes.length + 1);
                col.nodes[col.nodes.length - 1] = e.getRightNode();
            }
            rightRange.addNamedJoinColumnExpression(name, col);
        }
    }
    if (fullList && !joinColumnNames.containsAll(nameSet)) {
        throw Error.error(ErrorCode.X_42501);
    }
    rightRange.addNamedJoinColumns(joinColumnNames);
    return result;
}
