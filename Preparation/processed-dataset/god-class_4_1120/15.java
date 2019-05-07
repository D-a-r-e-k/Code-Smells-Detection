void convertToSimpleColumn(OrderedHashSet expressions, OrderedHashSet replacements) {
    if (opType == OpTypes.VALUE) {
        return;
    }
    int index = expressions.getIndex(this);
    if (index != -1) {
        Expression e = (Expression) replacements.get(index);
        nodes = emptyArray;
        opType = OpTypes.SIMPLE_COLUMN;
        columnIndex = e.columnIndex;
        rangePosition = e.rangePosition;
        return;
    }
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] == null) {
            continue;
        }
        nodes[i].convertToSimpleColumn(expressions, replacements);
    }
}
