/**
     * Returns the initial <em>minimum</em> price of the given goods
     * type. The initial price in a particular Market may be higher.
     *
     * @param goodsType a <code>GoodsType</code> value
     * @return an <code>int</code> value
     */
public int getInitialPrice(GoodsType goodsType) {
    String suffix = goodsType.getSuffix("model.goods.");
    if (hasOption("model.option." + suffix + ".minimumPrice") && hasOption("model.option." + suffix + ".maximumPrice")) {
        return Math.min(getInteger("model.option." + suffix + ".maximumPrice"), getInteger("model.option." + suffix + ".minimumPrice"));
    } else {
        return goodsType.getInitialSellPrice();
    }
}
