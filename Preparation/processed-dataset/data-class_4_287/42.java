public String getSQL() {
    StringBuffer sb = new StringBuffer();
    int limit;
    sb.append(Tokens.T_SELECT).append(' ');
    limit = indexLimitVisible;
    for (int i = 0; i < limit; i++) {
        if (i > 0) {
            sb.append(',');
        }
        sb.append(exprColumns[i].getSQL());
    }
    sb.append(Tokens.T_FROM);
    limit = rangeVariables.length;
    for (int i = 0; i < limit; i++) {
        RangeVariable rangeVar = rangeVariables[i];
        if (i > 0) {
            if (rangeVar.isLeftJoin && rangeVar.isRightJoin) {
                sb.append(Tokens.T_FULL).append(' ');
            } else if (rangeVar.isLeftJoin) {
                sb.append(Tokens.T_LEFT).append(' ');
            } else if (rangeVar.isRightJoin) {
                sb.append(Tokens.T_RIGHT).append(' ');
            }
            sb.append(Tokens.T_JOIN).append(' ');
        }
        sb.append(rangeVar.getTable().getName().statementName);
    }
    if (isGrouped) {
        sb.append(' ').append(Tokens.T_GROUP).append(' ').append(Tokens.T_BY);
        limit = indexLimitVisible + groupByColumnCount;
        for (int i = indexLimitVisible; i < limit; i++) {
            sb.append(exprColumns[i].getSQL());
            if (i < limit - 1) {
                sb.append(',');
            }
        }
    }
    if (havingCondition != null) {
        sb.append(' ').append(Tokens.T_HAVING).append(' ');
        sb.append(havingCondition.getSQL());
    }
    if (sortAndSlice.hasOrder()) {
        limit = indexStartOrderBy + sortAndSlice.getOrderLength();
        sb.append(' ').append(Tokens.T_ORDER).append(Tokens.T_BY).append(' ');
        for (int i = indexStartOrderBy; i < limit; i++) {
            sb.append(exprColumns[i].getSQL());
            if (i < limit - 1) {
                sb.append(',');
            }
        }
    }
    if (sortAndSlice.hasLimit()) {
        sb.append(sortAndSlice.limitCondition.getLeftNode().getSQL());
    }
    return sb.toString();
}
