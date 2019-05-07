/**
     * Returns the most valuable goods available in one of the
     * player's colonies for the purposes of choosing a
     * threat-to-boycott.  The goods must not currently be boycotted,
     * the player must have traded in it, and the amount to be discarded
     * will not exceed GoodsContainer.CARGO_SIZE.
     *
     * @return A goods object, or null.
     */
public Goods getMostValuableGoods() {
    if (!isEuropean())
        return null;
    Goods goods = null;
    int highValue = 0;
    for (Colony colony : getColonies()) {
        for (Goods g : colony.getCompactGoods()) {
            if (getArrears(g.getType()) <= 0 && hasTraded(g.getType())) {
                int amount = Math.min(g.getAmount(), GoodsContainer.CARGO_SIZE);
                int value = market.getSalePrice(g.getType(), amount);
                if (value > highValue) {
                    highValue = value;
                    goods = g;
                }
            }
        }
    }
    return goods;
}
