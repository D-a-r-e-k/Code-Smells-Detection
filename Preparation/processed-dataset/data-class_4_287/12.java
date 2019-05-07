private void resolveColumnReferencesForAsterisk() {
    for (int pos = 0; pos < indexLimitVisible; ) {
        Expression e = (Expression) (exprColumnList.get(pos));
        if (e.getType() == OpTypes.MULTICOLUMN) {
            exprColumnList.remove(pos);
            String tablename = ((ExpressionColumn) e).getTableName();
            if (tablename == null) {
                addAllJoinedColumns(e);
            } else {
                int rangeIndex = e.findMatchingRangeVariableIndex(rangeVariables);
                if (rangeIndex == -1) {
                    throw Error.error(ErrorCode.X_42501, tablename);
                }
                RangeVariable range = rangeVariables[rangeIndex];
                HashSet exclude = getAllNamedJoinColumns();
                range.addTableColumns(e, exclude);
            }
            for (int i = 0; i < e.nodes.length; i++) {
                exprColumnList.add(pos, e.nodes[i]);
                pos++;
            }
            indexLimitVisible += e.nodes.length - 1;
        } else {
            pos++;
        }
    }
}
