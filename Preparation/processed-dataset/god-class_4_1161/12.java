/**
     * Steals neighbouring tiles but only if the colony has some
     * defence.  Grab everything if at war with the owner, otherwise
     * just take the tile that best helps with the currently required
     * raw building materials, with a lesser interest in food.
     */
private void stealTiles() {
    final Specification spec = getSpecification();
    final Tile tile = colony.getTile();
    final Player player = colony.getOwner();
    boolean hasDefender = false;
    for (Unit u : tile.getUnitList()) {
        if (u.isDefensiveUnit() && getAIUnit(u).getMission() instanceof DefendSettlementMission) {
            // TODO: be smarter 
            hasDefender = true;
            break;
        }
    }
    if (!hasDefender)
        return;
    // What goods are really needed? 
    List<GoodsType> needed = new ArrayList<GoodsType>();
    for (GoodsType g : spec.getRawBuildingGoodsTypeList()) {
        if (colony.getTotalProductionOf(g) <= 0)
            needed.add(g);
    }
    // If a tile can be stolen, do so if already at war with the 
    // owner or if it is the best one available. 
    UnitType unitType = spec.getDefaultUnitType();
    Tile steal = null;
    float score = 1.0f;
    for (Tile t : tile.getSurroundingTiles(1)) {
        Player owner = t.getOwner();
        if (owner == null || owner == player || owner.isEuropean())
            continue;
        if (owner.atWarWith(player)) {
            if (AIMessage.askClaimLand(t, this, NetworkConstants.STEAL_LAND) && player.owns(t)) {
                logger.info(player.getName() + " stole tile " + t + " from hostile " + owner.getName());
            }
        } else {
            // Pick the best tile to steal, considering mainly the 
            // building goods needed, but including food at a lower 
            // weight. 
            float s = 0.0f;
            for (GoodsType g : needed) {
                s += t.potential(g, unitType);
            }
            for (GoodsType g : spec.getFoodGoodsTypeList()) {
                s += 0.1 * t.potential(g, unitType);
            }
            if (s > score) {
                score = s;
                steal = t;
            }
        }
    }
    if (steal != null) {
        Player owner = steal.getOwner();
        if (AIMessage.askClaimLand(steal, this, NetworkConstants.STEAL_LAND) && player.owns(steal)) {
            logger.info(player.getName() + " stole tile " + steal + " (score = " + score + ") from " + owner.getName());
        }
    }
}
