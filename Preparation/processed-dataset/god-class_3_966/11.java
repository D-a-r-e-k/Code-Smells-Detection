/**
     * Sets the {@link WhereNode where tree} for this query.
     * @param where a {@link WhereNode}
     * @throws IllegalStateException if I have already been resolved
     */
public void setWhere(WhereNode where) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    _where = where;
}
