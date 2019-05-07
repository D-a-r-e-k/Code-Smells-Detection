/**
     * Sets the data extract order (by row or by column) and sends a
     * {@link PlotChangeEvent}to all registered listeners.
     *
     * @param order the order (<code>null</code> not permitted).
     *
     * @throws IllegalArgumentException if <code>order</code> is
     *     <code>null</code>.
     *
     * @see #getDataExtractOrder()
     */
public void setDataExtractOrder(TableOrder order) {
    if (order == null) {
        throw new IllegalArgumentException("Null 'order' argument");
    }
    this.dataExtractOrder = order;
    fireChangeEvent();
}
