/**
     * Gets the first plan for a specified tile from a list of tile
     * improvement plans.
     *
     * @param tile The <code>Tile</code> to look for.
     * @param plans A list of <code>TileImprovementPlan</code>s to search.
     * @return A matching plan, or null if not found.
     */
private TileImprovementPlan getPlanFor(Tile tile, List<TileImprovementPlan> plans) {
    for (TileImprovementPlan tip : plans) {
        if (tip.getTarget() == tile)
            return tip;
    }
    return null;
}
