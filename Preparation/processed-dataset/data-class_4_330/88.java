/**
     * Builds a canSeeTiles array.
     *
     * Note that tiles must be tested for null as they may be both
     * valid tiles but yet null during a save game load.
     *
     * Note the use of copies of the unit and settlement lists to
     * avoid nasty surprises due to asynchronous disappearance of
     * members of either.  TODO: see if this can be relaxed.
     *
     * @param map The <code>Map</code> to use.
     * @return A canSeeTiles array.
     */
private boolean[][] makeCanSeeTiles(Map map) {
    final Specification spec = getSpecification();
    boolean[][] cST = new boolean[map.getWidth()][map.getHeight()];
    if (!spec.getBoolean(GameOptions.FOG_OF_WAR)) {
        for (Tile t : getGame().getMap().getAllTiles()) {
            if (t != null) {
                cST[t.getX()][t.getY()] = hasExplored(t);
            }
        }
    } else {
        for (Unit unit : getUnits()) {
            // Only consider units directly on the map, not those 
            // on a carrier or in Europe. 
            if (!(unit.getLocation() instanceof Tile))
                continue;
            Tile tile = (Tile) unit.getLocation();
            cST[tile.getX()][tile.getY()] = true;
            for (Tile t : tile.getSurroundingTiles(unit.getLineOfSight())) {
                if (t != null) {
                    cST[t.getX()][t.getY()] = hasExplored(t);
                }
            }
        }
        for (Settlement settlement : new ArrayList<Settlement>(getSettlements())) {
            Tile tile = settlement.getTile();
            cST[tile.getX()][tile.getY()] = true;
            for (Tile t : tile.getSurroundingTiles(settlement.getLineOfSight())) {
                if (t != null) {
                    cST[t.getX()][t.getY()] = hasExplored(t);
                }
            }
        }
        if (isEuropean() && spec.getBoolean(GameOptions.ENHANCED_MISSIONARIES)) {
            for (Player other : getGame().getPlayers()) {
                if (this.equals(other) || !other.isIndian())
                    continue;
                for (Settlement settlement : other.getSettlements()) {
                    IndianSettlement is = (IndianSettlement) settlement;
                    if (is.getMissionary(this) == null)
                        continue;
                    for (Tile t : is.getTile().getSurroundingTiles(is.getLineOfSight())) {
                        if (t != null) {
                            cST[t.getX()][t.getY()] = hasExplored(t);
                        }
                    }
                }
            }
        }
    }
    return cST;
}
