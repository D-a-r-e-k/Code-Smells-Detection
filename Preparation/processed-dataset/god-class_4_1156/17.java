/**
     * Places "high seas"-tiles on the border of the given map.
     *
     * @param map The <code>Map</code> to create high seas on.
     * @param distToLandFromHighSeas The distance between the land
     *      and the high seas (given in tiles).
     * @param maxDistanceToEdge The maximum distance a high sea tile
     *      can have from the edge of the map.
     */
private static void createHighSeas(Map map, int distToLandFromHighSeas, int maxDistanceToEdge) {
    if (distToLandFromHighSeas < 0 || maxDistanceToEdge < 0) {
        throw new IllegalArgumentException("The integer arguments cannot be negative.");
    }
    final Specification spec = map.getSpecification();
    final TileType ocean = spec.getTileType("model.tile.ocean");
    final TileType highSeas = spec.getTileType("model.tile.highSeas");
    if (highSeas == null) {
        throw new RuntimeException("TileType highSeas must be defined.");
    }
    Tile t, seaL = null, seaR = null;
    int totalL = 0, totalR = 0, distanceL = -1, distanceR = -1;
    for (int y = 0; y < map.getHeight(); y++) {
        for (int x = 0; x < maxDistanceToEdge && x < map.getWidth() && map.isValid(x, y) && (t = map.getTile(x, y)).getType() == ocean; x++) {
            Tile other = map.getLandWithinDistance(x, y, distToLandFromHighSeas);
            if (other == null) {
                t.setType(highSeas);
                totalL++;
            } else {
                int distance = t.getDistanceTo(other);
                if (distanceL < distance) {
                    distanceL = distance;
                    seaL = t;
                }
            }
        }
        for (int x = 0; x < maxDistanceToEdge && x < map.getWidth() && map.isValid(map.getWidth() - 1 - x, y) && (t = map.getTile(map.getWidth() - 1 - x, y)).getType() == ocean; x++) {
            Tile other = map.getLandWithinDistance(map.getWidth() - 1 - x, y, distToLandFromHighSeas);
            if (other == null) {
                t.setType(highSeas);
                totalR++;
            } else {
                int distance = t.getDistanceTo(other);
                if (distanceR < distance) {
                    distanceR = distance;
                    seaR = t;
                }
            }
        }
    }
    if (totalL <= 0 && seaL != null) {
        seaL.setType(highSeas);
        totalL++;
    }
    if (totalR <= 0 && seaR != null) {
        seaR.setType(highSeas);
        totalR++;
    }
    if (totalL <= 0 || totalR <= 0) {
        logger.warning("No high seas on " + ((totalL <= 0 && totalR <= 0) ? "either" : (totalL <= 0) ? "left" : (totalR <= 0) ? "right" : "BOGUS") + " side of the map." + "  This can cause failures on small test maps.");
    }
}
