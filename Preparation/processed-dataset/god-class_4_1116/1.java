// logical ops 
static Expression andExpressions(Expression e1, Expression e2) {
    if (e1 == null) {
        return e2;
    }
    if (e2 == null) {
        return e1;
    }
    if (ExpressionLogical.EXPR_FALSE.equals(e1) || ExpressionLogical.EXPR_FALSE.equals(e2)) {
        return ExpressionLogical.EXPR_FALSE;
    }
    return new ExpressionLogical(OpTypes.AND, e1, e2);
}
