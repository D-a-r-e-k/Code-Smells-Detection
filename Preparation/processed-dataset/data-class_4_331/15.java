/**
     * Finds a build plan for this type.
     *
     * @param type The <code>BuildableType</code> to search for.
     * @return A <code>BuildPlan</code> with this type, or null if not found.
     */
private BuildPlan findBuildPlan(BuildableType type) {
    for (BuildPlan bp : buildPlans) {
        if (bp.type == type)
            return bp;
    }
    return null;
}
