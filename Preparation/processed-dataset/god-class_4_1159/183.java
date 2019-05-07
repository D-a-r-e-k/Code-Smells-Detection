/**
     * Modifies the current sales.
     *
     * @param goodsType a <code>GoodsType</code> value
     * @param amount The new sales.
     */
public void modifySales(GoodsType goodsType, int amount) {
    getMarket().modifySales(goodsType, amount);
}
