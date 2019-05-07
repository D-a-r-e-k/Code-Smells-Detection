/**
     * Flood fill ocean regions.
     *
     * @param map The <code>Map</code> to fill in.
     * @param p A valid starting <code>Position</code>.
     * @param region A <code>ServerRegion</code> to fill with.
     * @param bounds A <code>Rectangle</code> that bounds the filling.
     * @return The number of tiles filled.
     */
private int fillOcean(Map map, Position p, ServerRegion region, Rectangle bounds) {
    Queue<Position> q = new LinkedList<Position>();
    int n = 0;
    boolean[][] visited = new boolean[map.getWidth()][map.getHeight()];
    visited[p.getX()][p.getY()] = true;
    q.add(p);
    while ((p = q.poll()) != null) {
        Tile tile = map.getTile(p);
        region.addTile(tile);
        n++;
        for (Direction direction : Direction.values()) {
            Position next = p.getAdjacent(direction);
            if (map.isValid(next) && !visited[next.getX()][next.getY()] && bounds.contains(next.getX(), next.getY())) {
                visited[next.getX()][next.getY()] = true;
                Tile t = map.getTile(next);
                if ((t.getRegion() == null || t.getRegion() == region) && !t.isLand()) {
                    q.add(next);
                }
            }
        }
    }
    return n;
}
