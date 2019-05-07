/**
     * Creates a random tile for the specified position.
     *
     * @param game The <code>Game</code> to create the tile in.
     * @param x The tile x coordinate.
     * @param y The tile y coordinate.
     * @param landMap A boolean array defining where the land is.
     * @param latitude The tile latitude.
     * @return The created tile.
     */
private Tile createTile(Game game, int x, int y, boolean[][] landMap, int latitude) {
    return (landMap[x][y]) ? new Tile(game, getRandomLandTileType(game, latitude), x, y) : new Tile(game, getRandomOceanTileType(game, latitude), x, y);
}
