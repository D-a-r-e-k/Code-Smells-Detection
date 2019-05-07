/**
     * Gets the <i>i</i><sup>th</sup> {@link OrderNode} in my order by clause.
     * Clients should treat the returned value as immutable.
     * @param i the zero-based index
     */
public OrderNode getOrderBy(int i) {
    return (OrderNode) (_orderBy.get(i));
}
