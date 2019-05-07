/**
     * Returns true if type of goods can be traded at specified place
     *
     * @param goods The goods.
     * @param access Place where the goods are traded (Europe OR Custom)
     * @return True if type of goods can be traded.
     */
public boolean canTrade(Goods goods, Market.Access access) {
    return canTrade(goods.getType(), access);
}
