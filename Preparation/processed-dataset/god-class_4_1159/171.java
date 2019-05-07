/**
     * Returns how many total liberty will be produced if no colonies
     * are lost and nothing unexpected happens.
     *
     * @return Total number of liberty this <code>Player</code>'s
     *         <code>Colony</code>s will make.
     * @see #incrementLiberty
     */
public int getLibertyProductionNextTurn() {
    int libertyNextTurn = 0;
    for (Colony colony : getColonies()) {
        for (GoodsType libertyGoods : getSpecification().getLibertyGoodsTypeList()) {
            libertyNextTurn += colony.getTotalProductionOf(libertyGoods);
        }
    }
    return libertyNextTurn;
}
