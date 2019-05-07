public boolean supportsOperator(ComparisonOperator op) {
    if (ComparisonOperator.EQUAL.equals(op)) {
        if (isUnique()) {
            return true;
        } else {
            return getIndexedColumn().getDataType().supportsSuccessor();
        }
    } else if (ComparisonOperator.GREATER_THAN.equals(op)) {
        return getIndexedColumn().getDataType().supportsSuccessor();
    } else if (ComparisonOperator.GREATER_THAN_OR_EQUAL.equals(op)) {
        return true;
    } else if (ComparisonOperator.LESS_THAN.equals(op)) {
        return true;
    } else if (ComparisonOperator.LESS_THAN_OR_EQUAL.equals(op)) {
        return getIndexedColumn().getDataType().supportsSuccessor();
    } else {
        return false;
    }
}
