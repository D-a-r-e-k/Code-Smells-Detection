/**
   * When building the SQL Document, we need to store the Expression
   * Context that was used to create the document. This will be se to
   * reference items int he XSLT process such as any variables that were
   * present.
   */
protected void setExpressionContext(ExpressionContext expr) {
    m_ExpressionContext = expr;
}
