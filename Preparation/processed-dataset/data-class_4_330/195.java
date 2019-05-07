/**
     * Gets the current sales data for a location and goods type.
     *
     * @param where The <code>Location</code> of the sale.
     * @param what The <code>GoodsType</code> sold.
     *
     * @return An appropriate <code>LastSaleData</code> record or null.
     */
public LastSale getLastSale(Location where, GoodsType what) {
    return (lastSales == null) ? null : lastSales.get(LastSale.makeKey(where, what));
}
