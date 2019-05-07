/**
     * Given a buildable that improves production of a goods type,
     * prioritize it.
     *
     * @param type The <code>BuildableType</code> to consider.
     * @param goodsType The <code>GoodsType</code> improved by the buildable.
     * @return True if this type was prioritized.
     */
private boolean prioritizeProduction(BuildableType type, GoodsType goodsType) {
    Player player = colony.getOwner();
    NationType nationType = player.getNationType();
    String advantage = getAIMain().getAIPlayer(player).getAIAdvantage();
    boolean ret = false;
    double factor = 1.0;
    if (!nationType.getModifierSet(goodsType.getId()).isEmpty()) {
        // Handles building, agriculture, furTrapping advantages 
        factor *= 1.2;
    }
    if (goodsType.isMilitaryGoods()) {
        if ("conquest".equals(advantage))
            factor = 1.2;
        ret = prioritize(type, MILITARY_WEIGHT * factor, 1.0);
    } else if (goodsType.isBuildingMaterial()) {
        ret = prioritize(type, BUILDING_WEIGHT * factor, 1.0);
    } else if (goodsType.isLibertyType()) {
        ret = prioritize(type, LIBERTY_WEIGHT, (colony.getSoL() >= 100) ? 0.01 : 1.0);
    } else if (goodsType.isImmigrationType()) {
        if ("immigration".equals(advantage))
            factor = 1.2;
        ret = prioritize(type, IMMIGRATION_WEIGHT * factor, 1.0);
    } else if (produce.contains(goodsType)) {
        if ("trade".equals(advantage))
            factor = 1.2;
        double f = 0.1 * colony.getTotalProductionOf(goodsType.getRawMaterial());
        ret = prioritize(type, PRODUCTION_WEIGHT, f);
    }
    return ret;
}
