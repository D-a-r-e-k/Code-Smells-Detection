/**
     * Returns the price of the given land.
     *
     * @param tile The <code>Tile</code> to get the price for.
     * @return The price of the land if it is for sale, zero if it is already
     *         ours, unclaimed or unwanted, negative if it is not for sale.
     */
public int getLandPrice(Tile tile) {
    final Specification spec = getSpecification();
    Player nationOwner = tile.getOwner();
    int price = 0;
    if (nationOwner == null || nationOwner == this) {
        return 0;
    } else if (tile.getSettlement() != null) {
        return -1;
    } else if (nationOwner.isEuropean()) {
        if (tile.getOwningSettlement() != null && tile.getOwningSettlement().getOwner() == nationOwner) {
            return -1;
        } else {
            return 0;
        }
    }
    // Else, native ownership 
    for (GoodsType type : spec.getGoodsTypeList()) {
        if (type == spec.getPrimaryFoodType()) {
            // Only consider specific food types, not the aggregation. 
            continue;
        }
        price += tile.potential(type, null);
    }
    price *= spec.getInteger("model.option.landPriceFactor");
    price += 100;
    return (int) applyModifier(price, "model.modifier.landPaymentModifier", null, getGame().getTurn());
}
