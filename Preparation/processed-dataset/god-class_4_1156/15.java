/**
     * Places "high seas"-tiles on the border of the given map.
     *
     * @param map The <code>Map</code> to create high seas on.
     */
private void createHighSeas(Map map) {
    OptionGroup opt = mapOptions;
    createHighSeas(map, opt.getInteger("model.option.distanceToHighSea"), opt.getInteger("model.option.maximumDistanceToEdge"));
}
