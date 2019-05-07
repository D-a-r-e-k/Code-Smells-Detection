/**
     * Modifies the current incomeAfterTaxes.
     *
     * @param goodsType The GoodsType.
     * @param amount The new incomeAfterTaxes.
     */
public void modifyIncomeAfterTaxes(GoodsType goodsType, int amount) {
    getMarket().modifyIncomeAfterTaxes(goodsType, amount);
}
