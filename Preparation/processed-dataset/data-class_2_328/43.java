/**
     * Describe <code>getGoodsType</code> method here.
     *
     * @param id a <code>String</code> value
     * @return a <code>GoodsType</code> value
     */
public GoodsType getGoodsType(String id) {
    return getType(id, GoodsType.class);
}
