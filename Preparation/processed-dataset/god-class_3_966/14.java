/**
     * Appends an {@link OrderNode} to the order by clause for this query
     * @param orderby an {@link OrderNode} to append
     * @throws IllegalStateException if I have already been resolved
     */
public void addOrderBy(OrderNode orderby) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    _orderBy.add(orderby);
}
