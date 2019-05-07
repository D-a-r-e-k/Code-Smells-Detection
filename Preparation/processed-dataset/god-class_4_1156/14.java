/**
     * Creates land map regions in the given Map.
     *
     * First, the arctic/antarctic regions are defined, based on
     * <code>LandGenerator.POLAR_HEIGHT</code>.
     *
     * For the remaining land tiles, one region per contiguous
     * landmass is created.
     *
     * @param map The <code>Map</code> to work on.
     */
private void createLandRegions(Map map) {
    Game game = map.getGame();
    // Create "explorable" land regions 
    int continents = 0;
    boolean[][] landmap = new boolean[map.getWidth()][map.getHeight()];
    int[][] continentmap = new int[map.getWidth()][map.getHeight()];
    int landsize = 0;
    // Initialize both maps 
    for (int x = 0; x < map.getWidth(); x++) {
        for (int y = 0; y < map.getHeight(); y++) {
            continentmap[x][y] = 0;
            landmap[x][y] = false;
            if (map.isValid(x, y)) {
                Tile tile = map.getTile(x, y);
                // Exclude existing regions (arctic/antarctic, mountains, 
                // rivers). 
                landmap[x][y] = tile.isLand() && tile.getRegion() == null;
                if (tile.isLand())
                    landsize++;
            }
        }
    }
    // Flood fill, so that we end up with individual landmasses 
    // numbered in continentmap[][] 
    for (int y = 0; y < map.getHeight(); y++) {
        for (int x = 0; x < map.getWidth(); x++) {
            if (landmap[x][y]) {
                // Found a new region. 
                continents++;
                boolean[][] continent = Map.floodFill(landmap, new Position(x, y));
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    for (int xx = 0; xx < map.getWidth(); xx++) {
                        if (continent[xx][yy]) {
                            continentmap[xx][yy] = continents;
                            landmap[xx][yy] = false;
                        }
                    }
                }
            }
        }
    }
    logger.info("Number of individual landmasses is " + continents);
    // Get landmass sizes 
    int[] continentsize = new int[continents + 1];
    for (int y = 0; y < map.getHeight(); y++) {
        for (int x = 0; x < map.getWidth(); x++) {
            continentsize[continentmap[x][y]]++;
        }
    }
    // Go through landmasses, split up those too big 
    int oldcontinents = continents;
    for (int c = 1; c <= oldcontinents; c++) {
        // c starting at 1, c=0 is all excluded tiles 
        if (continentsize[c] > LAND_REGION_MAX_SIZE) {
            boolean[][] splitcontinent = new boolean[map.getWidth()][map.getHeight()];
            Position splitposition = new Position(0, 0);
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    if (continentmap[x][y] == c) {
                        splitcontinent[x][y] = true;
                        splitposition = new Position(x, y);
                    } else {
                        splitcontinent[x][y] = false;
                    }
                }
            }
            while (continentsize[c] > LAND_REGION_MAX_SIZE) {
                int targetsize = LAND_REGION_MAX_SIZE;
                if (continentsize[c] < 2 * LAND_REGION_MAX_SIZE) {
                    targetsize = continentsize[c] / 2;
                }
                continents++;
                //index of the new region in continentmap[][] 
                boolean[][] newregion = Map.floodFill(splitcontinent, splitposition, targetsize);
                for (int x = 0; x < map.getWidth(); x++) {
                    for (int y = 0; y < map.getHeight(); y++) {
                        if (newregion[x][y]) {
                            continentmap[x][y] = continents;
                            splitcontinent[x][y] = false;
                            continentsize[c]--;
                        }
                        if (splitcontinent[x][y]) {
                            splitposition = new Position(x, y);
                        }
                    }
                }
            }
        }
    }
    logger.info("Number of land regions being created: " + continents);
    // Create ServerRegions for all land regions 
    ServerRegion[] landregions = new ServerRegion[continents + 1];
    int landIndex = 1;
    for (int c = 1; c <= continents; c++) {
        // c starting at 1, c=0 is all water tiles 
        String id;
        do {
            id = "model.region.land" + Integer.toString(landIndex++);
        } while (map.getRegion(id) != null);
        landregions[c] = new ServerRegion(map.getGame(), id, Region.RegionType.LAND, null);
        landregions[c].setDiscoverable(true);
        map.putRegion(landregions[c]);
    }
    // Add tiles to ServerRegions 
    for (int y = 0; y < map.getHeight(); y++) {
        for (int x = 0; x < map.getWidth(); x++) {
            if (continentmap[x][y] > 0) {
                Tile tile = map.getTile(x, y);
                landregions[continentmap[x][y]].addTile(tile);
            }
        }
    }
    for (int c = 1; c <= continents; c++) {
        ServerRegion sr = landregions[c];
        // Set exploration points for land regions based on size 
        int score = Math.max((int) (((float) sr.getSize() / landsize) * LAND_REGIONS_SCORE_VALUE), LAND_REGION_MIN_SCORE);
        sr.setScoreValue(score);
        setGeographicRegion(sr);
        logger.fine("Created land region " + sr.getNameKey() + " (size " + sr.getSize() + ", score " + sr.getScoreValue() + ", parent " + ((sr.getParent() == null) ? "(null)" : sr.getParent().getNameKey()) + ")");
    }
    for (ServerRegion gr : geographicRegions) {
        logger.fine("Geographic region " + gr.getNameKey() + " (size " + gr.getSize() + ", children " + gr.getChildren().size() + ")");
    }
}
