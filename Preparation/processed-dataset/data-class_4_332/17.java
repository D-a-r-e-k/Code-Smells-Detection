/**
     * Emits a standard message regarding the state of AIGoods.
     *
     * @param ag The <code>AIGoods</code> to log.
     * @param action The state of the goods.
     */
private void goodsLog(AIGoods ag, String action) {
    Goods goods = (ag == null) ? null : ag.getGoods();
    int amount = (goods == null) ? -1 : goods.getAmount();
    String type = (goods == null) ? "(null)" : Utils.lastPart(ag.getGoods().getType().getId(), ".");
    logger.finest(String.format("%-20s %-10s %s %s %s", colony.getName(), action, ((ag == null) ? "(null)" : ag.getId()), ((amount >= GoodsContainer.CARGO_SIZE) ? "full" : Integer.toString(amount)), type));
}
