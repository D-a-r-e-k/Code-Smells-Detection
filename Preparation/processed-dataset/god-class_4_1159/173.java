/**
     * Returns the arrears due for a type of goods.
     *
     * @param type a <code>GoodsType</code> value
     * @return The arrears due for this type of goods.
     */
public int getArrears(GoodsType type) {
    return getMarket().getArrears(type);
}
