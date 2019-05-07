/**
     * Gets a random ocean tile type.
     *
     * @param game The <code>Game</code> to query for tile types.
     * @param latitude The latitude of the proposed tile.
     * @return A suitable random ocean tile type.
     */
private TileType getRandomOceanTileType(Game game, int latitude) {
    if (oceanTileTypes == null) {
        oceanTileTypes = new ArrayList<TileType>();
        for (TileType type : game.getSpecification().getTileTypeList()) {
            if (type.isWater() && type.isHighSeasConnected() && !type.isDirectlyHighSeasConnected()) {
                oceanTileTypes.add(type);
            }
        }
    }
    return getRandomTileType(game, oceanTileTypes, latitude);
}
