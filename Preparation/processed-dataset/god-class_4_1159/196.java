/**
     * Gets the last sale price for a location and goods type as a string.
     *
     * @param where The <code>Location</code> of the sale.
     * @param what The <code>GoodsType</code> sold.
     * @return An abbreviation for the sale price, or null if none found.
     */
public String getLastSaleString(Location where, GoodsType what) {
    LastSale data = getLastSale(where, what);
    return (data == null) ? null : String.valueOf(data.getPrice());
}
