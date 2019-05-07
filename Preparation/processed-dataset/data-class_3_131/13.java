/**
     * Sets the order by clause for this query.
     * @param orderby a {@link List} of {@link OrderNode}s.
     * @throws IllegalStateException if I have already been resolved
     */
public void setOrderBy(List orderby) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    _orderBy = orderby;
}
