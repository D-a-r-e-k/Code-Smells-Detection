public String describe(Session session, int blanks) {
    StringBuffer sb;
    String temp;
    String b = ValuePool.spaceString.substring(0, blanks);
    sb = new StringBuffer();
    sb.append(b).append("isDistinctSelect=[").append(isDistinctSelect).append("]\n");
    sb.append(b).append("isGrouped=[").append(isGrouped).append("]\n");
    sb.append(b).append("isAggregated=[").append(isAggregated).append("]\n");
    sb.append(b).append("columns=[");
    for (int i = 0; i < indexLimitVisible; i++) {
        int index = i;
        if (exprColumns[i].getType() == OpTypes.SIMPLE_COLUMN) {
            index = exprColumns[i].columnIndex;
        }
        sb.append(b).append(exprColumns[index].describe(session, 2));
    }
    sb.append("\n");
    sb.append(b).append("]\n");
    for (int i = 0; i < rangeVariables.length; i++) {
        sb.append(b).append("[");
        sb.append("range variable ").append(i + 1).append("\n");
        sb.append(rangeVariables[i].describe(session, blanks + 2));
        sb.append(b).append("]");
    }
    sb.append(b).append("]\n");
    temp = queryCondition == null ? "null" : queryCondition.describe(session, blanks);
    if (isGrouped) {
        sb.append(b).append("groupColumns=[");
        for (int i = indexLimitRowId; i < indexLimitRowId + groupByColumnCount; i++) {
            int index = i;
            if (exprColumns[i].getType() == OpTypes.SIMPLE_COLUMN) {
                index = exprColumns[i].columnIndex;
            }
            sb.append(exprColumns[index].describe(session, blanks));
        }
        sb.append(b).append("]\n");
    }
    if (havingCondition != null) {
        temp = havingCondition.describe(session, blanks);
        sb.append(b).append("havingCondition=[").append(temp).append("]\n");
    }
    if (sortAndSlice.hasOrder()) {
        sb.append(b).append("order by=[\n");
        for (int i = 0; i < sortAndSlice.exprList.size(); i++) {
            sb.append(b).append(((Expression) sortAndSlice.exprList.get(i)).describe(session, blanks));
            sb.append(b).append("\n]");
        }
        sb.append(b).append("]\n");
    }
    if (sortAndSlice.hasLimit()) {
        if (sortAndSlice.limitCondition.getLeftNode() != null) {
            sb.append(b).append("offset=[").append(sortAndSlice.limitCondition.getLeftNode().describe(session, 0)).append("]\n");
        }
        if (sortAndSlice.limitCondition.getRightNode() != null) {
            sb.append(b).append("limit=[").append(sortAndSlice.limitCondition.getRightNode().describe(session, 0)).append("]\n");
        }
    }
    return sb.toString();
}
