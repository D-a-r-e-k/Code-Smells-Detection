/**
     * Returns a Select object that can be used for checking the contents
     * of an existing table against the given CHECK search condition.
     */
static QuerySpecification getCheckSelect(Session session, Table t, Expression e) {
    CompileContext compileContext = new CompileContext(session, null);
    QuerySpecification s = new QuerySpecification(compileContext);
    RangeVariable[] ranges = new RangeVariable[] { new RangeVariable(t, null, null, null, compileContext) };
    e.resolveCheckOrGenExpression(session, ranges, true);
    s.exprColumns = new Expression[1];
    s.exprColumns[0] = EXPR_TRUE;
    s.rangeVariables = ranges;
    if (Type.SQL_BOOLEAN != e.getDataType()) {
        throw Error.error(ErrorCode.X_42568);
    }
    Expression condition = new ExpressionLogical(OpTypes.NOT, e);
    s.queryCondition = condition;
    s.resolveReferences(session);
    s.resolveTypes(session);
    return s;
}
