/**
     * Gets the list of tiles that might be claimable by a settlement.
     * We can not do a simple iteration of the rings because this
     * allows settlements to claim tiles across unclaimable gaps
     * (e.g. Aztecs owning tiles on nearby islands).  So we have to
     * only allow tiles that are adjacent to a known connected tile.
     *
     * @param centerTile The intended settlement center <code>Tile</code>.
     * @param radius The radius of the settlement.
     * @return A list of potentially claimable tiles.
     */
public List<Tile> getClaimableTiles(Tile centerTile, int radius) {
    List<Tile> tiles = new ArrayList<Tile>();
    List<Tile> layer = new ArrayList<Tile>();
    if (canClaimToFoundSettlement(centerTile)) {
        layer.add(centerTile);
        for (int r = 1; r <= radius; r++) {
            List<Tile> lastLayer = new ArrayList<Tile>(layer);
            tiles.addAll(layer);
            layer.clear();
            for (Tile have : lastLayer) {
                for (Tile next : have.getSurroundingTiles(1)) {
                    if (!tiles.contains(next) && canClaimForSettlement(next)) {
                        layer.add(next);
                    }
                }
            }
        }
        tiles.addAll(layer);
    }
    return tiles;
}
