/**
     * Returns the current incomeBeforeTaxes.
     *
     * @param goodsType The GoodsType.
     * @return The current incomeBeforeTaxes.
     */
public int getIncomeBeforeTaxes(GoodsType goodsType) {
    return getMarket().getIncomeBeforeTaxes(goodsType);
}
