/**
     * Returns the current incomeAfterTaxes.
     *
     * @param goodsType The GoodsType.
     * @return The current incomeAfterTaxes.
     */
public int getIncomeAfterTaxes(GoodsType goodsType) {
    return getMarket().getIncomeAfterTaxes(goodsType);
}
