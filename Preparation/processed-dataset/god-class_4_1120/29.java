/**
     * return the expression for an alias used in an ORDER BY clause
     */
Expression replaceAliasInOrderBy(Expression[] columns, int length) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] == null) {
            continue;
        }
        nodes[i] = nodes[i].replaceAliasInOrderBy(columns, length);
    }
    return this;
}
