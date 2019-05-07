/**
     * Gets a random land tile type based on the latitude.
     *
     * @param game the Game
     * @param latitude The location of the tile relative to the north/south
     *     poles and equator:
     *     0 is the mid-section of the map (equator)
     *     +/-90 is on the bottom/top of the map (poles).
     * @return A suitable random land tile type.
     */
private TileType getRandomLandTileType(Game game, int latitude) {
    if (landTileTypes == null) {
        landTileTypes = new ArrayList<TileType>();
        for (TileType type : game.getSpecification().getTileTypeList()) {
            if (type.isElevation() || type.isWater()) {
                // do not generate elevated and water tiles at this time 
                // they are created separately 
                continue;
            }
            landTileTypes.add(type);
        }
    }
    return getRandomTileType(game, landTileTypes, latitude);
}
