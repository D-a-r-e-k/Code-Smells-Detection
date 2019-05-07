/**
     * Creates mountain ranges on the given map.  The number and size
     * of mountain ranges depends on the map size.
     *
     * @param map The map to use.
     */
private void createMountains(Map map) {
    float randomHillsRatio = 0.5f;
    // 50% of user settings will be allocated for random hills 
    // here and there the rest will be allocated for large 
    // mountain ranges 
    int maximumLength = Math.max(mapOptions.getInteger("model.option.mapWidth"), mapOptions.getInteger("model.option.mapHeight")) / 10;
    int number = (int) ((getApproximateLandCount() / mapOptions.getInteger("model.option.mountainNumber")) * (1 - randomHillsRatio));
    logger.info("Number of mountain tiles is " + number);
    logger.fine("Maximum length of mountain ranges is " + maximumLength);
    // lookup the resources from specification 
    final Specification spec = map.getSpecification();
    TileType hills = spec.getTileType("model.tile.hills");
    TileType mountains = spec.getTileType("model.tile.mountains");
    if (hills == null || mountains == null) {
        throw new RuntimeException("Both Hills and Mountains TileTypes must be defined");
    }
    // Generate the mountain ranges 
    int counter = 0;
    nextTry: for (int tries = 0; tries < 100; tries++) {
        if (counter < number) {
            Tile startTile = getRandomLandTile(map, random);
            if (startTile == null)
                return;
            // No land found! 
            if (startTile.getType() == hills || startTile.getType() == mountains) {
                // already a high ground 
                continue;
            }
            // do not start a mountain range too close to another 
            for (Tile t : startTile.getSurroundingTiles(3)) {
                if (t.getType() == mountains)
                    continue nextTry;
            }
            // Do not add a mountain range too close to the 
            // ocean/lake this helps with good locations for 
            // building colonies on shore 
            for (Tile t : startTile.getSurroundingTiles(2)) {
                if (!t.isLand())
                    continue nextTry;
            }
            ServerRegion mountainRegion = new ServerRegion(map.getGame(), "model.region.mountain" + tries, Region.RegionType.MOUNTAIN, startTile.getRegion());
            mountainRegion.setDiscoverable(true);
            mountainRegion.setClaimable(true);
            map.putRegion(mountainRegion);
            Direction direction = Direction.getRandomDirection("getLand", random);
            int length = maximumLength - random.nextInt(maximumLength / 2);
            for (int index = 0; index < length; index++) {
                Tile nextTile = startTile.getNeighbourOrNull(direction);
                if (nextTile == null || !nextTile.isLand())
                    continue;
                nextTile.setType(mountains);
                mountainRegion.addTile(nextTile);
                counter++;
                for (Tile neighbour : nextTile.getSurroundingTiles(1)) {
                    if (!neighbour.isLand() || neighbour.getType() == mountains)
                        continue;
                    int r = random.nextInt(8);
                    if (r == 0) {
                        neighbour.setType(mountains);
                        mountainRegion.addTile(neighbour);
                        counter++;
                    } else if (r > 2) {
                        neighbour.setType(hills);
                        mountainRegion.addTile(neighbour);
                    }
                }
            }
            int scoreValue = 2 * mountainRegion.getSize();
            mountainRegion.setScoreValue(scoreValue);
            logger.fine("Created mountain region (direction " + direction + ", length " + length + ", size " + mountainRegion.getSize() + ", score value " + scoreValue + ").");
        }
    }
    logger.info("Added " + counter + " mountain range tiles.");
    // and sprinkle a few random hills/mountains here and there 
    number = (int) (getApproximateLandCount() * randomHillsRatio) / mapOptions.getInteger("model.option.mountainNumber");
    counter = 0;
    nextTry: for (int tries = 0; tries < 1000; tries++) {
        if (counter < number) {
            Tile t = getRandomLandTile(map, random);
            if (t == null)
                return;
            if (t.getType() == hills || t.getType() == mountains) {
                continue;
            }
            // Do not add hills too close to a mountain range 
            // this would defeat the purpose of adding random hills. 
            for (Tile tile : t.getSurroundingTiles(3)) {
                if (tile.getType() == mountains)
                    continue nextTry;
            }
            // Do not add hills too close to the ocean/lake this 
            // helps with good locations for building colonies on 
            // shore. 
            for (Tile tile : t.getSurroundingTiles(1)) {
                if (!tile.isLand())
                    continue nextTry;
            }
            // 25% mountains, 75% hills 
            t.setType((random.nextInt(4) == 0) ? mountains : hills);
            counter++;
        }
    }
    logger.info("Added " + counter + " random hills tiles.");
}
