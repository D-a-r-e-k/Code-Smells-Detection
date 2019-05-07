/**
     * Requires a goods wish for a specified goods type be present at this
     * AI colony.  If one is already present, the amount specified here
     * takes precedence as it is more likely to be up to date.  The value
     * is treated as a minimum requirement.
     *
     * @param type The <code>GoodsType</code> to wish for.
     * @param amount The amount of goods wished for.
     * @param value The urgency of the wish.
     */
public void requireGoodsWish(GoodsType type, int amount, int value) {
    GoodsWish gw = null;
    for (Wish w : wishes) {
        if (w instanceof GoodsWish && ((GoodsWish) w).getGoodsType() == type) {
            gw = (GoodsWish) w;
            break;
        }
    }
    if (gw != null) {
        gw.setGoodsAmount(amount);
        gw.setValue(value);
    } else {
        gw = new GoodsWish(getAIMain(), colony, value, amount, type);
        wishes.add(gw);
        logger.finest(colony.getName() + " makes new goods wish: " + gw);
    }
}
