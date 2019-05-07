/**
     * Create a random resource on a tile.
     *
     * @param tile The <code>Tile</code> to create the resource on.
     * @return The created resource, or null if it is not possible.
     */
private Resource createResource(Tile tile) {
    if (tile == null)
        return null;
    ResourceType resourceType = RandomChoice.getWeightedRandom(null, null, random, tile.getType().getWeightedResources());
    if (resourceType == null)
        return null;
    int minValue = resourceType.getMinValue();
    int maxValue = resourceType.getMaxValue();
    int quantity = (minValue == maxValue) ? maxValue : (minValue + random.nextInt(maxValue - minValue + 1));
    return new Resource(tile.getGame(), tile, resourceType, quantity);
}
