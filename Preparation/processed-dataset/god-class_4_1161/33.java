/**
     * Removes a <code>TileImprovementPlan</code> from the list
     *
     * @return True if it was successfully deleted, false otherwise
     */
public boolean removeTileImprovementPlan(TileImprovementPlan plan) {
    return tileImprovementPlans.remove(plan);
}
