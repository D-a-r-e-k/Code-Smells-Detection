/**
     * Gets the value of building a <code>Colony</code> on the given tile.
     * This method adds bonuses to the colony value if the tile is close to (but
     * not overlapping with) another friendly colony. Penalties for enemy
     * units/colonies are added as well.
     *
     * @param tile The <code>Tile</code>
     * @return The value of building a colony on the given tile.
     */
public int getColonyValue(Tile tile) {
    //----- TODO: tune magic numbers 
    //applied once 
    final float MOD_HAS_RESOURCE = 0.75f;
    final float MOD_NO_PATH = 0.5f;
    final float MOD_LONG_PATH = 0.75f;
    final float MOD_FOOD_LOW = 0.75f;
    final float MOD_FOOD_VERY_LOW = 0.5f;
    //applied per goods 
    final float MOD_BUILD_MATERIAL_MISSING = 0.10f;
    //applied per surrounding tile 
    final float MOD_ADJ_SETTLEMENT_BIG = 0.25f;
    final float MOD_ADJ_SETTLEMENT = 0.5f;
    final float MOD_OWNED_EUROPEAN = 0.8f;
    final float MOD_OWNED_NATIVE = 0.9f;
    //applied per goods production, per surrounding tile 
    final float MOD_HIGH_PRODUCTION = 1.2f;
    final float MOD_GOOD_PRODUCTION = 1.1f;
    //applied per occurrence (own colony only one-time), range-dependent. 
    final float[] MOD_OWN_COLONY = { 0.0f, 0.0f, 0.5f, 1.50f, 1.25f };
    final float[] MOD_ENEMY_COLONY = { 0.0f, 0.0f, 0.4f, 0.5f, 0.7f };
    final float[] MOD_NEUTRAL_COLONY = { 0.0f, 0.0f, 0.7f, 0.8f, 1.0f };
    final float[] MOD_ENEMY_UNIT = { 0.0f, 0.5f, 0.6f, 0.75f, 0.9f };
    final int LONG_PATH_TILES = 16;
    final int PRIMARY_GOODS_VALUE = 30;
    //goods production in excess of this on a tile counts as good/high 
    final int GOOD_PRODUCTION = 4;
    final int HIGH_PRODUCTION = 8;
    //counting "high" production as 2, "good" production as 1 
    //overall food production is considered low/very low if less than... 
    final int FOOD_LOW = 4;
    final int FOOD_VERY_LOW = 2;
    //----- END MAGIC NUMBERS 
    // Return -INFINITY if there is a settlement here or neighbouring. 
    for (Tile t : tile.getSurroundingTiles(0, 1)) {
        if (t.getSettlement() != null)
            return -INFINITY;
    }
    //initialize tile value 
    int value = 0;
    if (tile.getType().getPrimaryGoods() != null) {
        value += tile.potential(tile.getType().getPrimaryGoods().getType(), null) * PRIMARY_GOODS_VALUE;
    }
    //value += tile.potential(tile.secondaryGoods(), null) * tile.secondaryGoods().getInitialSellPrice(); 
    //multiplicative modifier, to be applied to value later 
    float advantage = 1f;
    //set up maps for all foods and building materials 
    final Specification spec = getSpecification();
    TypeCountMap<GoodsType> rawBuildingMaterialMap = new TypeCountMap<GoodsType>();
    for (GoodsType g : spec.getRawBuildingGoodsTypeList()) {
        rawBuildingMaterialMap.incrementCount(g, 0);
    }
    TypeCountMap<GoodsType> foodMap = new TypeCountMap<GoodsType>();
    for (GoodsType g : spec.getFoodGoodsTypeList()) {
        foodMap.incrementCount(g, 0);
    }
    // Penalty for building on a resource tile, because production 
    // can not be improved much. 
    if (tile.hasResource())
        advantage *= MOD_HAS_RESOURCE;
    // Penalty if there is no direct connection to the high seas, or 
    // if it is too long. 
    int tilesToHighSeas = Integer.MAX_VALUE;
    for (Tile n : tile.getSurroundingTiles(1)) {
        int v = tile.getHighSeasCount();
        if (v >= 0 && v < tilesToHighSeas)
            tilesToHighSeas = v;
    }
    if (tilesToHighSeas == Integer.MAX_VALUE) {
        advantage *= MOD_NO_PATH;
    } else if (tilesToHighSeas > LONG_PATH_TILES) {
        advantage *= MOD_LONG_PATH;
    }
    boolean supportingColony = false;
    for (int radius = 1; radius < 5; radius++) {
        for (Tile t : getGame().getMap().getCircleTiles(tile, false, radius)) {
            Settlement set = t.getSettlement();
            //may be null! 
            Colony col = t.getColony();
            //may be null! 
            if (radius == 1) {
                //already checked: no colony here - if set!=null, it's indian 
                if (set != null) {
                    //penalize building next to native settlement 
                    SettlementType type = set.getType();
                    if (type.getClaimableRadius() > 1) {
                        // really shouldn't build next to cities 
                        advantage *= MOD_ADJ_SETTLEMENT_BIG;
                    } else {
                        advantage *= MOD_ADJ_SETTLEMENT;
                    }
                } else {
                    //apply penalty for owned neighbouring tiles 
                    if (t.getOwner() != null && !this.owns(t)) {
                        if (t.getOwner().isEuropean()) {
                            advantage *= MOD_OWNED_EUROPEAN;
                        } else {
                            advantage *= MOD_OWNED_NATIVE;
                        }
                    }
                    //count production 
                    if (t.getType() != null) {
                        for (AbstractGoods production : t.getType().getProduction()) {
                            GoodsType type = production.getType();
                            int potential = t.potential(type, null);
                            value += potential * type.getInitialSellPrice();
                            // a few tiles with high production are better 
                            // than many tiles with low production 
                            int highProductionValue = 0;
                            if (potential > HIGH_PRODUCTION) {
                                advantage *= MOD_HIGH_PRODUCTION;
                                highProductionValue = 2;
                            } else if (potential > GOOD_PRODUCTION) {
                                advantage *= MOD_GOOD_PRODUCTION;
                                highProductionValue = 1;
                            }
                            if (type.isFoodType()) {
                                foodMap.incrementCount(type, highProductionValue);
                            } else if (type.isRawBuildingMaterial()) {
                                rawBuildingMaterialMap.incrementCount(type, highProductionValue);
                            }
                        }
                    }
                }
            } else {
                if (value <= 0) {
                    //value no longer changes, so return if still <=0 
                    return 0;
                }
                if (col != null) {
                    //apply modifier for own colony at this distance 
                    if (col.getOwner() == this) {
                        if (!supportingColony) {
                            supportingColony = true;
                            advantage *= MOD_OWN_COLONY[radius];
                        }
                    } else {
                        if (atWarWith(col.getOwner())) {
                            advantage *= MOD_ENEMY_COLONY[radius];
                        } else {
                            advantage *= MOD_NEUTRAL_COLONY[radius];
                        }
                    }
                }
            }
            for (Unit u : t.getUnitList()) {
                if (u.getOwner() != this && u.isOffensiveUnit() && u.getOwner().isEuropean() && atWarWith(u.getOwner())) {
                    advantage *= MOD_ENEMY_UNIT[radius];
                }
            }
        }
    }
    //check availability of key goods 
    for (GoodsType type : rawBuildingMaterialMap.keySet()) {
        Integer amount = rawBuildingMaterialMap.getCount(type);
        if (amount == 0) {
            advantage *= MOD_BUILD_MATERIAL_MISSING;
        }
    }
    int foodProduction = 0;
    for (Integer food : foodMap.values()) {
        foodProduction += food;
    }
    if (foodProduction < FOOD_VERY_LOW) {
        advantage *= MOD_FOOD_VERY_LOW;
    } else if (foodProduction < FOOD_LOW) {
        advantage *= MOD_FOOD_LOW;
    }
    return (int) (value * advantage);
}
