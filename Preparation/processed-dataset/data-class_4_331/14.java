/**
     * Chooses the two best raw materials, updating the production
     * map and lists.
     *
     * @param production The production map.
     */
private void updateRawMaterials(Map<GoodsType, Map<WorkLocation, Integer>> production) {
    Player player = colony.getOwner();
    Market market = player.getMarket();
    NationType nationType = player.getNationType();
    GoodsType primaryRawMaterial = null;
    GoodsType secondaryRawMaterial = null;
    int primaryValue = -1;
    int secondaryValue = -1;
    produce.clear();
    List<GoodsType> rawMaterials = new ArrayList<GoodsType>(rawLuxuryGoodsTypes);
    rawMaterials.addAll(otherRawGoodsTypes);
    for (GoodsType g : rawMaterials) {
        int value = 0;
        for (Entry<WorkLocation, Integer> e : production.get(g).entrySet()) {
            value += e.getValue().intValue();
        }
        if (value <= LOW_PRODUCTION_THRESHOLD) {
            production.remove(g);
            continue;
        }
        if (market != null) {
            // If the market is available, weight by sale price of 
            // the material, or if it is the raw material for a 
            // refined goods type, the average of the raw and 
            // refined goods prices. 
            if (g.getProducedMaterial() == null) {
                value *= market.getSalePrice(g, 1);
            } else if (production.containsKey(g.getProducedMaterial())) {
                value *= (market.getSalePrice(g, 1) + market.getSalePrice(g.getProducedMaterial(), 1)) / 2;
            }
        }
        if (!nationType.getModifierSet(g.getId()).isEmpty()) {
            value = (value * 12) / 10;
        }
        if (value > secondaryValue && secondaryRawMaterial != null) {
            production.remove(secondaryRawMaterial);
            production.remove(secondaryRawMaterial.getProducedMaterial());
            if (rawLuxuryGoodsTypes.contains(secondaryRawMaterial)) {
                rawLuxuryGoodsTypes.remove(secondaryRawMaterial);
                luxuryGoodsTypes.remove(secondaryRawMaterial.getProducedMaterial());
            } else if (rawMaterials.contains(otherRawGoodsTypes)) {
                rawMaterials.remove(otherRawGoodsTypes);
            }
        }
        if (value > primaryValue) {
            secondaryRawMaterial = primaryRawMaterial;
            secondaryValue = primaryValue;
            primaryRawMaterial = g;
            primaryValue = value;
        } else if (value > secondaryValue) {
            secondaryRawMaterial = g;
            secondaryValue = value;
        }
    }
    if (primaryRawMaterial != null) {
        produce.add(primaryRawMaterial);
        if (primaryRawMaterial.getProducedMaterial() != null) {
            produce.add(primaryRawMaterial.getProducedMaterial());
        }
        if (secondaryRawMaterial != null) {
            produce.add(secondaryRawMaterial);
            if (secondaryRawMaterial.getProducedMaterial() != null) {
                produce.add(secondaryRawMaterial.getProducedMaterial());
            }
        }
    }
}
