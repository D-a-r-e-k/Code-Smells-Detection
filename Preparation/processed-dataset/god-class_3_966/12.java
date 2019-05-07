/**
     * Returns the {@link WhereNode where tree} for this query.
     * Clients should treat the returned value as immutable.
     * @return the {@link WhereNode where tree} for this query, or <tt>null</tt>.
     */
public WhereNode getWhere() {
    return _where;
}
