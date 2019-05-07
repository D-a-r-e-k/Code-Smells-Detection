/**
     * Gets the price to this player for a proposed unit.
     *
     * @param au The proposed <code>AbstractUnit</code>.
     * @return The price for the unit.
     */
public int getPrice(AbstractUnit au) {
    Specification spec = getSpecification();
    UnitType unitType = au.getUnitType(spec);
    if (unitType.hasPrice()) {
        int price = getEurope().getUnitPrice(unitType);
        for (EquipmentType equip : au.getEquipment(spec)) {
            for (AbstractGoods goods : equip.getRequiredGoods()) {
                price += getMarket().getBidPrice(goods.getType(), goods.getAmount());
            }
        }
        return price * au.getNumber();
    } else {
        return INFINITY;
    }
}
