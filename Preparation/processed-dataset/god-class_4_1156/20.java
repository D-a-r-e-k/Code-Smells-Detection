/**
     * Finds all the lake regions.
     *
     * @param map The <code>Map</code> to work on.
     */
private void createLakeRegions(Map map) {
    Game game = map.getGame();
    final TileType lakeType = map.getSpecification().getTileType("model.tile.lake");
    // Create the water map, and find any tiles that are water but 
    // not part of any region (such as the oceans).  These are 
    // lake tiles. 
    List<Tile> lakes = new ArrayList<Tile>();
    for (int y = 0; y < map.getHeight(); y++) {
        for (int x = 0; x < map.getWidth(); x++) {
            Tile tile;
            if (map.isValid(x, y) && !(tile = map.getTile(x, y)).isLand() && map.getTile(x, y).getRegion() == null) {
                lakes.add(tile);
                logger.info("Adding lake at " + x + "," + y);
            }
        }
    }
    // Make lake regions from unassigned lake tiles. 
    int lakeCount = 0;
    while (!lakes.isEmpty()) {
        Tile tile = lakes.get(0);
        if (tile.getRegion() != null)
            continue;
        String id;
        while (game.getFreeColGameObject(id = "model.region.inlandLake" + lakeCount) != null) lakeCount++;
        ServerRegion lakeRegion = new ServerRegion(game, id, RegionType.LAKE, null);
        setGeographicRegion(lakeRegion);
        map.putRegion(lakeRegion);
        // Pretend lakes are discovered with the surrounding terrain? 
        lakeRegion.setPrediscovered(false);
        List<Tile> todo = new ArrayList<Tile>();
        todo.add(tile);
        while (!todo.isEmpty()) {
            Tile t = todo.remove(0);
            if (lakes.contains(t)) {
                t.setRegion(lakeRegion);
                t.setType(lakeType);
                lakes.remove(t);
                todo.addAll(t.getSurroundingTiles(1, 1));
            }
        }
    }
}
