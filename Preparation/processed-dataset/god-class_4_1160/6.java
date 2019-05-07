/**
     * Gets the best buildable type from this plan that can currently
     * be built by the colony.
     *
     * @return The best current <code>BuildableType</code>.
     */
public BuildableType getBestBuildableType() {
    for (BuildPlan b : buildPlans) {
        if (colony.canBuild(b.type))
            return b.type;
    }
    return null;
}
