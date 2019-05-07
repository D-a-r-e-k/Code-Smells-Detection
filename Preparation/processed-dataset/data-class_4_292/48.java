int getDegree() {
    switch(opType) {
        case OpTypes.ROW:
            return nodes.length;
        case OpTypes.ROW_SUBQUERY:
        case OpTypes.TABLE_SUBQUERY:
            if (subQuery == null) {
            }
            return subQuery.queryExpression.getColumnCount();
        default:
            return 1;
    }
}
