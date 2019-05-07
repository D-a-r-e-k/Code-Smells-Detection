/**
     * Returns the arrears due for a type of goods.
     *
     * @param goods The goods.
     * @return The arrears due for this type of goods.
     */
public int getArrears(Goods goods) {
    return getArrears(goods.getType());
}
