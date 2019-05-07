/**
     * Gets a copy of the current list of buildable types associated
     * with this <code>ColonyPlan</code>.
     *
     * @return A copy of the of <code>BuildableType</code>s list.
     */
public List<BuildableType> getBuildableTypes() {
    List<BuildableType> build = new ArrayList<BuildableType>();
    for (BuildPlan b : buildPlans) build.add(b.type);
    return build;
}
