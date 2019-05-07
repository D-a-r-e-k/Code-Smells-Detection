/**
     * Calculates the value of an outpost-type colony at this tile.
     * An "outpost" is supposed to be a colony containing one worker, exporting
     * its whole production to europe. The value of such colony is the maximum
     * amount of money it can make in one turn, assuming sale of its secondary
     * goods plus farmed goods from one of the surrounding tiles.
     *
     * @return The value of a future colony located on this tile. This value is
     *         used by the AI when deciding where to build a new colony.
     */
public int getOutpostValue(Tile t) {
    Market market = getMarket();
    if (canClaimToFoundSettlement(t)) {
        boolean nearbyTileIsOcean = false;
        float advantages = 1f;
        int value = 0;
        for (Tile tile : t.getSurroundingTiles(1)) {
            if (tile.getColony() != null) {
                // can't build next to colony 
                return 0;
            } else if (tile.getSettlement() != null) {
                // can build next to an indian settlement, but shouldn't 
                SettlementType type = tile.getSettlement().getType();
                if (type.getClaimableRadius() > 1) {
                    // really shouldn't build next to cities 
                    advantages *= 0.25f;
                } else {
                    advantages *= 0.5f;
                }
            } else {
                if (tile.isHighSeasConnected()) {
                    nearbyTileIsOcean = true;
                }
                if (tile.getType() != null) {
                    for (AbstractGoods production : tile.getType().getProduction()) {
                        GoodsType type = production.getType();
                        int potential = market.getSalePrice(type, tile.potential(type, null));
                        if (tile.getOwner() != null && !this.owns(tile)) {
                            // tile is already owned by someone (and not by us!) 
                            if (tile.getOwner().isEuropean()) {
                                continue;
                            } else {
                                potential /= 2;
                            }
                        }
                        value = Math.max(value, potential);
                    }
                }
            }
        }
        //add secondary goods being produced by a colony on this tile 
        if (t.getType().getSecondaryGoods() != null) {
            GoodsType secondary = t.getType().getSecondaryGoods().getType();
            value += market.getSalePrice(secondary, t.potential(secondary, null));
        }
        if (nearbyTileIsOcean) {
            return Math.max(0, (int) (value * advantages));
        }
    }
    return 0;
}
