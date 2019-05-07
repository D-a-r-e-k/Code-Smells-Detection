/**
     * Updates the goods type lists.  The categories are:<UL>
     * <LI>food</LI>
     * <LI>liberty</LI>
     * <LI>immigration</LI>
     * <LI>military</LI>
     * <LI>raw building</LI>
     * <LI>building</LI>
     * <LI>raw luxury</LI>
     * <LI>luxury</LI>
     * <LI>raw other</LI>
     * </UL>
     *
     * Ignore raw materials which can not be refined and refined goods
     * that have no raw materials available.  Also ignore other goods
     * that do not fit these categories (e.g. trade goods).
     *
     * @param production The production map.
     */
private void updateGoodsTypeLists(Map<GoodsType, Map<WorkLocation, Integer>> production) {
    foodGoodsTypes.clear();
    libertyGoodsTypes.clear();
    immigrationGoodsTypes.clear();
    militaryGoodsTypes.clear();
    rawBuildingGoodsTypes.clear();
    buildingGoodsTypes.clear();
    rawLuxuryGoodsTypes.clear();
    luxuryGoodsTypes.clear();
    otherRawGoodsTypes.clear();
    for (GoodsType g : new ArrayList<GoodsType>(production.keySet())) {
        if (g.isFoodType()) {
            foodGoodsTypes.add(g);
        } else if (g.isLibertyType()) {
            libertyGoodsTypes.add(g);
        } else if (g.isImmigrationType()) {
            immigrationGoodsTypes.add(g);
        } else if (g.isMilitaryGoods()) {
            militaryGoodsTypes.add(g);
        } else if (g.isRawBuildingMaterial()) {
            rawBuildingGoodsTypes.add(g);
        } else if (g.isBuildingMaterial() && g.getRawMaterial().isRawBuildingMaterial()) {
            buildingGoodsTypes.add(g);
        } else if (g.isNewWorldGoodsType()) {
            rawLuxuryGoodsTypes.add(g);
        } else if (g.isRefined() && g.getRawMaterial().isNewWorldGoodsType()) {
            luxuryGoodsTypes.add(g);
        } else if (g.isFarmed()) {
            otherRawGoodsTypes.add(g);
        } else {
            // Not interested in this goods type. 
            logger.warning("Ignoring goods type " + g + " at " + colony.getName());
            production.remove(g);
        }
    }
}
