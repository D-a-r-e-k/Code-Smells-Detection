/**
     * Creates a <code>Map</code> for the given <code>Game</code>.
     *
     * The <code>Map</code> is added to the <code>Game</code> after
     * it is created.
     *
     * @param game The <code>Game</code> to add the map to.
     * @param importGame The <code>Game</code> to import information form.
     * @param landMap Determines whether there should be land or ocean
     *     on a given tile.  This array also specifies the size of the
     *     map that is going to be created.
     * @see Map
     */
public void createMap(Game game, Game importGame, boolean[][] landMap) {
    final Specification spec = game.getSpecification();
    final int width = landMap.length;
    final int height = landMap[0].length;
    final boolean importTerrain = (importGame != null) && mapOptions.getBoolean(MapGeneratorOptions.IMPORT_TERRAIN);
    final boolean importBonuses = (importGame != null) && mapOptions.getBoolean(MapGeneratorOptions.IMPORT_BONUSES);
    boolean mapHasLand = false;
    Tile[][] tiles = new Tile[width][height];
    Map map = new Map(game, tiles);
    int minimumLatitude = mapOptions.getInteger(MapGeneratorOptions.MINIMUM_LATITUDE);
    int maximumLatitude = mapOptions.getInteger(MapGeneratorOptions.MAXIMUM_LATITUDE);
    // make sure the values are in range 
    minimumLatitude = limitToRange(minimumLatitude, -90, 90);
    maximumLatitude = limitToRange(maximumLatitude, -90, 90);
    map.setMinimumLatitude(Math.min(minimumLatitude, maximumLatitude));
    map.setMaximumLatitude(Math.max(minimumLatitude, maximumLatitude));
    java.util.Map<String, ServerRegion> regionMap = new HashMap<String, ServerRegion>();
    if (importTerrain) {
        // Import the regions 
        String ids = "";
        for (Region r : importGame.getMap().getRegions()) {
            ServerRegion region = new ServerRegion(game, r);
            map.putRegion(region);
            regionMap.put(r.getId(), region);
            ids += " " + region.getNameKey();
        }
        for (Region r : importGame.getMap().getRegions()) {
            ServerRegion region = regionMap.get(r.getId());
            Region x = r.getParent();
            if (x != null)
                x = regionMap.get(x.getId());
            region.setParent(x);
            for (Region c : r.getChildren()) {
                x = regionMap.get(c.getId());
                if (x != null)
                    region.addChild(x);
            }
        }
        logger.info("Imported regions: " + ids);
    }
    List<Tile> fixRegions = new ArrayList<Tile>();
    for (int y = 0; y < height; y++) {
        int latitude = map.getLatitude(y);
        for (int x = 0; x < width; x++) {
            if (landMap[x][y])
                mapHasLand = true;
            Tile t, importTile = null;
            if (importTerrain && importGame.getMap().isValid(x, y) && (importTile = importGame.getMap().getTile(x, y)) != null && importTile.isLand() == landMap[x][y]) {
                String id = importTile.getType().getId();
                t = new Tile(game, spec.getTileType(id), x, y);
                if (importTile.getMoveToEurope() != null) {
                    t.setMoveToEurope(importTile.getMoveToEurope());
                }
                if (importTile.getTileItemContainer() != null) {
                    TileItemContainer container = new TileItemContainer(game, t);
                    // TileItemContainer copies every natural item 
                    // including Resource unless importBonuses == 
                    // false Rumors and roads are not copied 
                    container.copyFrom(importTile.getTileItemContainer(), importBonuses, true);
                    t.setTileItemContainer(container);
                }
                Region r = importTile.getRegion();
                if (r == null) {
                    fixRegions.add(t);
                } else {
                    ServerRegion ours = regionMap.get(r.getId());
                    if (ours == null) {
                        logger.warning("Could not set tile region " + r.getId() + " for tile: " + t);
                        fixRegions.add(t);
                    } else {
                        ours.addTile(t);
                    }
                }
            } else {
                t = createTile(game, x, y, landMap, latitude);
            }
            tiles[x][y] = t;
        }
    }
    game.setMap(map);
    geographicRegions = getStandardRegions(map);
    if (importTerrain) {
        if (!fixRegions.isEmpty()) {
            // Fix the tiles missing regions. 
            createOceanRegions(map);
            createLakeRegions(map);
            createLandRegions(map);
        }
    } else {
        createOceanRegions(map);
        createHighSeas(map);
        if (mapHasLand) {
            createMountains(map);
            createRivers(map);
            createLakeRegions(map);
            createLandRegions(map);
        }
    }
    // Add the bonuses only after the map is completed. 
    // Otherwise we risk creating resources on fields where they 
    // don't belong (like sugar in large rivers or tobaco on hills). 
    for (Tile tile : map.getAllTiles()) {
        perhapsAddBonus(tile, !importBonuses);
        if (!tile.isLand()) {
            encodeStyle(tile);
        }
    }
    map.resetContiguity();
    map.resetHighSeasCount();
}
