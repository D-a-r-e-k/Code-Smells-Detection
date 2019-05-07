/**
     * Has a type of goods been traded?
     *
     * @param goodsType a <code>GoodsType</code> value
     * @return Whether these goods have been traded.
     */
public boolean hasTraded(GoodsType goodsType) {
    return getMarket().hasBeenTraded(goodsType);
}
