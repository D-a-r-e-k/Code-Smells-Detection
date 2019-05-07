/**
     * Returns the current sales.
     *
     * @param goodsType a <code>GoodsType</code> value
     * @return The current sales.
     */
public int getSales(GoodsType goodsType) {
    return getMarket().getSales(goodsType);
}
