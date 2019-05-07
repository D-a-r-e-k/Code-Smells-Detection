/**
     * Makes sure we have the standard regions.
     *
     * @param map The <code>Map</code> to work on.
     * @return An array of the standard geographic regions.
     */
private ServerRegion[] getStandardRegions(Map map) {
    final Game game = map.getGame();
    // Create arctic/antarctic regions first, but only if they do 
    // not exist in on the map already.  This allows for example 
    // the imported Caribbean map to have arctic/antarctic regions 
    // defined but with no tiles assigned to them, thus they will 
    // not be seen on the map.  Generated games though will not have 
    // the region defined, and so will create it here. 
    final int arcticHeight = Map.POLAR_HEIGHT;
    final int antarcticHeight = map.getHeight() - Map.POLAR_HEIGHT - 1;
    ServerRegion arctic = (ServerRegion) map.getRegion("model.region.arctic");
    if (arctic == null) {
        arctic = new ServerRegion(game, "model.region.arctic", RegionType.LAND, null);
        arctic.setPrediscovered(true);
        map.putRegion(arctic);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < arcticHeight; y++) {
                if (map.isValid(x, y)) {
                    Tile tile = map.getTile(x, y);
                    if (tile.isLand())
                        arctic.addTile(tile);
                }
            }
        }
    }
    ServerRegion antarctic = (ServerRegion) map.getRegion("model.region.antarctic");
    if (antarctic == null) {
        antarctic = new ServerRegion(game, "model.region.antarctic", RegionType.LAND, null);
        antarctic.setPrediscovered(true);
        map.putRegion(antarctic);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = antarcticHeight; y < map.getHeight(); y++) {
                if (map.isValid(x, y)) {
                    Tile tile = map.getTile(x, y);
                    if (tile.isLand())
                        antarctic.addTile(tile);
                }
            }
        }
    }
    // Then, create "geographic" land regions.  These regions are 
    // used by the MapGenerator to place Indian Settlements.  Note 
    // that these regions are "virtual", i.e. having a bounding 
    // box, but containing no tiles directly. 
    final int thirdWidth = map.getWidth() / 3;
    final int twoThirdWidth = 2 * thirdWidth;
    final int thirdHeight = map.getHeight() / 3;
    final int twoThirdHeight = 2 * thirdHeight;
    ServerRegion northWest = (ServerRegion) map.getRegion("model.region.northWest");
    if (northWest == null) {
        northWest = new ServerRegion(game, "model.region.northWest", RegionType.LAND, null);
        map.putRegion(northWest);
    }
    northWest.setBounds(new Rectangle(0, 0, thirdWidth, thirdHeight));
    northWest.setPrediscovered(true);
    ServerRegion north = (ServerRegion) map.getRegion("model.region.north");
    if (north == null) {
        north = new ServerRegion(game, "model.region.north", RegionType.LAND, null);
        map.putRegion(north);
    }
    north.setBounds(new Rectangle(thirdWidth, 0, twoThirdWidth, thirdHeight));
    north.setPrediscovered(true);
    ServerRegion northEast = (ServerRegion) map.getRegion("model.region.northEast");
    if (northEast == null) {
        northEast = new ServerRegion(game, "model.region.northEast", RegionType.LAND, null);
        map.putRegion(northEast);
    }
    northEast.setBounds(new Rectangle(twoThirdWidth, 0, map.getWidth(), thirdHeight));
    northEast.setPrediscovered(true);
    ServerRegion west = (ServerRegion) map.getRegion("model.region.west");
    if (west == null) {
        west = new ServerRegion(game, "model.region.west", RegionType.LAND, null);
        map.putRegion(west);
    }
    west.setBounds(new Rectangle(0, thirdHeight, thirdWidth, twoThirdHeight));
    west.setPrediscovered(true);
    ServerRegion center = (ServerRegion) map.getRegion("model.region.center");
    if (center == null) {
        center = new ServerRegion(game, "model.region.center", RegionType.LAND, null);
        map.putRegion(center);
    }
    center.setBounds(new Rectangle(thirdWidth, thirdHeight, twoThirdWidth, twoThirdHeight));
    center.setPrediscovered(true);
    ServerRegion east = (ServerRegion) map.getRegion("model.region.east");
    if (east == null) {
        east = new ServerRegion(game, "model.region.east", RegionType.LAND, null);
        map.putRegion(east);
    }
    east.setBounds(new Rectangle(twoThirdWidth, thirdHeight, map.getWidth(), twoThirdHeight));
    east.setPrediscovered(true);
    ServerRegion southWest = (ServerRegion) map.getRegion("model.region.southWest");
    if (southWest == null) {
        southWest = new ServerRegion(game, "model.region.southWest", RegionType.LAND, null);
        map.putRegion(southWest);
    }
    southWest.setBounds(new Rectangle(0, twoThirdHeight, thirdWidth, map.getHeight()));
    southWest.setPrediscovered(true);
    ServerRegion south = (ServerRegion) map.getRegion("model.region.south");
    if (south == null) {
        south = new ServerRegion(game, "model.region.south", RegionType.LAND, null);
        map.putRegion(south);
    }
    south.setBounds(new Rectangle(thirdWidth, twoThirdHeight, twoThirdWidth, map.getHeight()));
    south.setPrediscovered(true);
    ServerRegion southEast = (ServerRegion) map.getRegion("model.region.southEast");
    if (southEast == null) {
        southEast = new ServerRegion(game, "model.region.southEast", RegionType.LAND, null);
        map.putRegion(southEast);
    }
    southEast.setBounds(new Rectangle(twoThirdWidth, twoThirdHeight, map.getWidth(), map.getHeight()));
    southEast.setPrediscovered(true);
    return new ServerRegion[] { northWest, north, northEast, west, center, east, southWest, south, southEast };
}
