/**
     * Select a random land tile on the map.
     *
     * @param map The <code>Map</code> to search in.
     * @param random A <code>Random</code> number source.
     * @return A random land tile, or null if none found.
     */
public Tile getRandomLandTile(Map map, Random random) {
    int x = (map.getWidth() < 10) ? random.nextInt(map.getWidth()) : random.nextInt(map.getWidth() - 10) + 5;
    int y = (map.getHeight() < 10) ? random.nextInt(map.getHeight()) : random.nextInt(map.getHeight() - 10) + 5;
    for (Tile t : map.getCircleTiles(map.getTile(x, y), true, FreeColObject.INFINITY)) {
        if (t.isLand())
            return t;
    }
    return null;
}
