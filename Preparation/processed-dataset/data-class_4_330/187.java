/**
     * Modifies the current incomeBeforeTaxes.
     *
     * @param goodsType The GoodsType.
     * @param amount The new incomeBeforeTaxes.
     */
public void modifyIncomeBeforeTaxes(GoodsType goodsType, int amount) {
    getMarket().modifyIncomeBeforeTaxes(goodsType, amount);
}
