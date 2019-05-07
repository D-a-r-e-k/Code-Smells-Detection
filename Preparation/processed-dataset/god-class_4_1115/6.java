void addHavingExpression(Expression e) {
    exprColumnList.add(e);
    havingCondition = e;
    havingColumnCount = 1;
}
