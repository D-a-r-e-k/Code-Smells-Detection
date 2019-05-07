/**
     * Set the players trade routes.
     *
     * @param newTradeRoutes The new list of <code>TradeRoute</code>s.
     *
     */
public final void setTradeRoutes(final List<TradeRoute> newTradeRoutes) {
    tradeRoutes.clear();
    tradeRoutes.addAll(newTradeRoutes);
}
