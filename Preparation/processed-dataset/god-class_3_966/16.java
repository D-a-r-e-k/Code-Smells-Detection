/**
     * Gets the number of {@link OrderNode}s in my query.
     */
public int getOrderByCount() {
    return (null == _orderBy ? 0 : _orderBy.size());
}
