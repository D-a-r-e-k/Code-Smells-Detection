/**
     * Places "high seas"-tiles on the border of the given map.
     *
     * Public so it can be called from an action when the distance
     * parameters are changed.
     *
     * All other tiles previously of type High Seas will be set to Ocean.
     *
     * @param map The <code>Map</code> to create high seas on.
     * @param distToLandFromHighSeas The distance between the land
     *      and the high seas (given in tiles).
     * @param maxDistanceToEdge The maximum distance a high sea tile
     *      can have from the edge of the map.
     */
public static void determineHighSeas(Map map, int distToLandFromHighSeas, int maxDistanceToEdge) {
    final Specification spec = map.getSpecification();
    final TileType ocean = spec.getTileType("model.tile.ocean");
    final TileType highSeas = spec.getTileType("model.tile.highSeas");
    if (highSeas == null || ocean == null) {
        throw new RuntimeException("Both Ocean and HighSeas TileTypes must be defined");
    }
    // Reset all highSeas tiles to the default ocean type. 
    for (Tile t : map.getAllTiles()) {
        if (t.getType() == highSeas)
            t.setType(ocean);
    }
    // Recompute the new high seas layout. 
    createHighSeas(map, distToLandFromHighSeas, maxDistanceToEdge);
}
