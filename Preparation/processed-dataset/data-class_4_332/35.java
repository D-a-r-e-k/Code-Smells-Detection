/**
     * Creates a list of the <code>Tile</code>-improvements which will
     * increase the production by this <code>Colony</code>.
     *
     * @see TileImprovementPlan
     */
public void createTileImprovementPlans() {
    List<TileImprovementPlan> newPlans = new ArrayList<TileImprovementPlan>();
    for (WorkLocation wl : colony.getAvailableWorkLocations()) {
        if (!(wl instanceof ColonyTile))
            continue;
        ColonyTile colonyTile = (ColonyTile) wl;
        Tile workTile = colonyTile.getWorkTile();
        if (workTile.getOwningSettlement() != colony || getPlanFor(workTile, newPlans) != null)
            continue;
        // Require food for the center tile, but otherwise insist 
        // the tile is being used, and try to improve the 
        // production that is underway. 
        GoodsType goodsType = null;
        if (colonyTile.isColonyCenterTile()) {
            for (AbstractGoods ag : colonyTile.getProduction()) {
                if (ag.getType().isFoodType()) {
                    goodsType = ag.getType();
                    break;
                }
            }
        } else {
            if (colonyTile.isEmpty())
                continue;
            goodsType = colonyTile.getUnitList().get(0).getWorkType();
        }
        if (goodsType == null)
            continue;
        TileImprovementPlan plan = getPlanFor(workTile, tileImprovementPlans);
        if (plan == null) {
            TileImprovementType type = TileImprovementPlan.getBestTileImprovementType(workTile, goodsType);
            if (type != null) {
                plan = new TileImprovementPlan(getAIMain(), workTile, type, type.getImprovementValue(workTile, goodsType));
            }
        } else {
            if (!plan.update(goodsType))
                plan = null;
        }
        if (plan != null) {
            // Defend against clearing the last forested tile, but 
            // otherwise add the plan. 
            TileType change = plan.getType().getChange(workTile.getType());
            if (change != null && !change.isForested()) {
                int forest = 0;
                for (WorkLocation f : colony.getAvailableWorkLocations()) {
                    if (f instanceof ColonyTile && ((ColonyTile) f).getWorkTile().isForested())
                        forest++;
                }
                if (forest <= FOREST_MINIMUM)
                    continue;
            }
            newPlans.add(plan);
            logger.info(colony.getName() + " new tile improvement plan: " + plan);
        }
    }
    tileImprovementPlans.clear();
    tileImprovementPlans.addAll(newPlans);
    Collections.sort(tileImprovementPlans);
}
