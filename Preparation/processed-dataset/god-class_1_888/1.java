public void parseComplete() {
    parenthesized = (ExpressionAST) getFirstChild();
    parenthesized.parseComplete();
}
