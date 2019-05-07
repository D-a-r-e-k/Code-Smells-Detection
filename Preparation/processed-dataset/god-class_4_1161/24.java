/**
     * Gets the goods wishes this colony has.
     *
     * @return A copy of the wishes list with non-goods wishes removed.
     */
public List<GoodsWish> getGoodsWishes() {
    List<GoodsWish> result = new ArrayList<GoodsWish>();
    for (Wish wish : wishes) {
        if (wish instanceof GoodsWish) {
            result.add((GoodsWish) wish);
        }
    }
    return result;
}
