/**
     * Returns true if type of goods can be traded in Europe.
     *
     * @param goods The goods.
     * @return True if there are no arrears due for this type of goods.
     */
public boolean canTrade(Goods goods) {
    return canTrade(goods, Market.Access.EUROPE);
}
