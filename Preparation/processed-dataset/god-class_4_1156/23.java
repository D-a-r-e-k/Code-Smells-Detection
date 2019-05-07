/**
     * Sets the style of the tiles.
     * Only relevant to water tiles for now.
     * Public because it is used in the river generator.
     *
     * @param tile The <code>Tile</code> to set the style of.
     */
public static void encodeStyle(Tile tile) {
    EnumMap<Direction, Boolean> connections = new EnumMap<Direction, Boolean>(Direction.class);
    // corners 
    for (Direction d : Direction.corners) {
        Tile t = tile.getNeighbourOrNull(d);
        connections.put(d, t != null && t.isLand());
    }
    // edges 
    for (Direction d : Direction.longSides) {
        Tile t = tile.getNeighbourOrNull(d);
        if (t != null && t.isLand()) {
            connections.put(d, true);
            // ignore adjacent corners 
            connections.put(d.getNextDirection(), false);
            connections.put(d.getPreviousDirection(), false);
        } else {
            connections.put(d, false);
        }
    }
    int result = 0;
    int index = 0;
    for (Direction d : Direction.corners) {
        if (connections.get(d))
            result += (int) Math.pow(2, index);
        index++;
    }
    for (Direction d : Direction.longSides) {
        if (connections.get(d))
            result += (int) Math.pow(2, index);
        index++;
    }
    tile.setStyle(result);
}
