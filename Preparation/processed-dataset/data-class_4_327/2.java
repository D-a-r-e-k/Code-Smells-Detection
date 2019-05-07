/**
     * Gets the approximate number of land tiles.
     *
     * @return The approximate number of land tiles
     */
private int getApproximateLandCount() {
    return mapOptions.getInteger("model.option.mapWidth") * mapOptions.getInteger("model.option.mapHeight") * mapOptions.getInteger("model.option.landMass") / 100;
}
