/**
     * {@inheritDoc}
     */
public String toString() {
    final Tile tile = colony.getTile();
    final StringBuilder sb = new StringBuilder();
    sb.append("ColonyPlan: " + colony.getName() + " " + colony.getTile().getPosition() + "\nProfile: " + profileType.toString() + "\nPreferred production:\n");
    for (GoodsType goodsType : getPreferredProduction()) {
        sb.append(goodsType.toString().substring(12) + "\n");
    }
    sb.append(getBuildableReport());
    sb.append("Food Plans:\n");
    for (WorkLocationPlan wlp : getFoodPlans()) {
        WorkLocation wl = wlp.getWorkLocation();
        String wlStr = (wl instanceof Building) ? ((Building) wl).getType().toString().substring(15) : (wl instanceof ColonyTile) ? tile.getDirection(((ColonyTile) wl).getWorkTile()).toString() : wl.getId();
        sb.append(wlStr + ": " + getWorkLocationProduction(wl, wlp.getGoodsType()) + " " + wlp.getGoodsType().toString().substring(12) + "\n");
    }
    sb.append("Work Plans:\n");
    for (WorkLocationPlan wlp : getWorkPlans()) {
        WorkLocation wl = wlp.getWorkLocation();
        String wlStr = (wl instanceof Building) ? ((Building) wl).getType().toString().substring(15) : (wl instanceof ColonyTile) ? tile.getDirection(((ColonyTile) wl).getWorkTile()).toString() : wl.getId();
        sb.append(wlStr + ": " + getWorkLocationProduction(wl, wlp.getGoodsType()) + " " + wlp.getGoodsType().toString().substring(12) + "\n");
    }
    return sb.toString();
}
