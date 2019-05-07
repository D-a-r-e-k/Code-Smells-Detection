/**
     * Updates the goods wishes.
     */
private void updateGoodsWishes() {
    final Specification spec = getSpecification();
    int goodsWishValue = 50;
    // Request goods. 
    // TODO: improve heuristics 
    TypeCountMap<GoodsType> required = new TypeCountMap<GoodsType>();
    // Add building materials. 
    if (colony.getCurrentlyBuilding() != null) {
        for (AbstractGoods ag : colony.getCurrentlyBuilding().getRequiredGoods()) {
            if (colony.getAdjustedNetProductionOf(ag.getType()) <= 0) {
                required.incrementCount(ag.getType(), ag.getAmount());
            }
        }
    }
    // Add materials required to improve tiles. 
    for (TileImprovementPlan plan : tileImprovementPlans) {
        for (AbstractGoods ag : plan.getType().getExpendedEquipmentType().getRequiredGoods()) {
            required.incrementCount(ag.getType(), ag.getAmount());
        }
    }
    // Add raw materials for buildings. 
    for (WorkLocation workLocation : colony.getCurrentWorkLocations()) {
        if (workLocation instanceof Building) {
            Building building = (Building) workLocation;
            GoodsType inputType = building.getGoodsInputType();
            ProductionInfo info = colony.getProductionInfo(building);
            if (inputType != null && info != null && !info.hasMaximumProduction()) {
                // TODO: find better heuristics 
                required.incrementCount(inputType, 100);
            }
        }
    }
    // Add breedable goods 
    for (GoodsType g : spec.getGoodsTypeList()) {
        if (g.isBreedable() && colony.getGoodsCount(g) < g.getBreedingNumber()) {
            required.incrementCount(g, g.getBreedingNumber());
        }
    }
    // Add materials required to build military equipment, 
    // but make sure there is a unit present that can use it. 
    if (isBadlyDefended()) {
        for (EquipmentType type : spec.getEquipmentTypeList()) {
            if (!type.isMilitaryEquipment())
                continue;
            for (Unit unit : colony.getTile().getUnitList()) {
                if (!unit.canBeEquippedWith(type))
                    continue;
                for (AbstractGoods ag : type.getRequiredGoods()) {
                    required.incrementCount(ag.getType(), ag.getAmount());
                }
                break;
            }
        }
    }
    // Drop wishes that are no longer needed. 
    int i = 0;
    while (i < wishes.size()) {
        if (wishes.get(i) instanceof GoodsWish) {
            GoodsWish g = (GoodsWish) wishes.get(i);
            GoodsType t = g.getGoodsType();
            if (required.getCount(t) < colony.getGoodsCount(t)) {
                completeWish(g, "redundant");
                continue;
            }
        }
        i++;
    }
    // Require wishes for what is missing. 
    for (GoodsType type : required.keySet()) {
        GoodsType requiredType = type;
        while (requiredType != null) {
            if (requiredType.isStorable())
                break;
            requiredType = requiredType.getRawMaterial();
        }
        if (requiredType == null)
            continue;
        int amount = Math.min(colony.getWarehouseCapacity(), (required.getCount(type) - colony.getGoodsCount(requiredType)));
        if (amount > 0) {
            int value = goodsWishValue;
            if (colonyCouldProduce(requiredType))
                value /= 10;
            requireGoodsWish(requiredType, amount, value);
        }
    }
}
