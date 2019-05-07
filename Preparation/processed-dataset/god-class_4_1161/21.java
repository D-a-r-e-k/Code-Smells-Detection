/**
     * Tries to complete any wishes for some goods that have just arrived.
     *
     * @param goods Some <code>Goods</code> that are arriving in this colony.
     * @return True if a wish was successfully completed.
     */
public boolean completeWish(Goods goods) {
    boolean ret = false;
    int i = 0;
    while (i < wishes.size()) {
        if (wishes.get(i) instanceof GoodsWish) {
            GoodsWish gw = (GoodsWish) wishes.get(i);
            if (gw.satisfiedBy(goods) && completeWish(gw, "satisfied(" + goods + ")")) {
                ret = true;
                continue;
            }
        }
        i++;
    }
    return ret;
}
