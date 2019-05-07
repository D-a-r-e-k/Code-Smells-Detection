/**
     * Returns true if type of goods can be traded in Europe.
     *
     * @param type The goods type.
     * @return True if there are no arrears due for this type of goods.
     */
public boolean canTrade(GoodsType type) {
    return canTrade(type, Market.Access.EUROPE);
}
