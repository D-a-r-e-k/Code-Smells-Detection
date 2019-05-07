/**
     * Creates rivers on the given map. The number of rivers depends
     * on the map size.
     *
     * @param map The <code>Map</code> to create rivers on.
     */
private void createRivers(Map map) {
    final Specification spec = map.getSpecification();
    final TileImprovementType riverType = spec.getTileImprovementType("model.improvement.river");
    final int number = getApproximateLandCount() / mapOptions.getInteger("model.option.riverNumber");
    int counter = 0;
    HashMap<Position, River> riverMap = new HashMap<Position, River>();
    List<River> rivers = new ArrayList<River>();
    for (int i = 0; i < number; i++) {
        nextTry: for (int tries = 0; tries < 100; tries++) {
            Tile tile = getRandomLandTile(map, random);
            if (tile == null)
                return;
            if (!tile.getType().canHaveImprovement(riverType)) {
                continue;
            }
            // check the river source/spring is not too close to the ocean 
            for (Tile neighborTile : tile.getSurroundingTiles(2)) {
                if (!neighborTile.isLand()) {
                    continue nextTry;
                }
            }
            if (riverMap.get(tile.getPosition()) == null) {
                // no river here yet 
                ServerRegion riverRegion = new ServerRegion(map.getGame(), "model.region.river" + i, Region.RegionType.RIVER, tile.getRegion());
                riverRegion.setDiscoverable(true);
                riverRegion.setClaimable(true);
                River river = new River(map, riverMap, riverRegion, random);
                if (river.flowFromSource(tile.getPosition())) {
                    logger.fine("Created new river with length " + river.getLength());
                    map.putRegion(riverRegion);
                    rivers.add(river);
                    counter++;
                } else {
                    logger.fine("Failed to generate river.");
                }
                break;
            }
        }
    }
    logger.info("Created " + counter + " rivers of maximum " + number);
    for (River river : rivers) {
        ServerRegion region = river.getRegion();
        int scoreValue = 0;
        for (RiverSection section : river.getSections()) {
            scoreValue += section.getSize();
        }
        scoreValue *= 2;
        region.setScoreValue(scoreValue);
        logger.fine("Created river region (length " + river.getLength() + ", score value " + scoreValue + ").");
    }
}
