/**
     * Adds or improves the priority of a buildable in a list.
     *
     * @param type The <code>BuildableType</code> to use.
     * @param weight The relative weight of this class of buildable with
     *     respect to other buildable classes.
     * @param support The support for this buildable within its class.
     * @return True if this type was prioritized.
     */
private boolean prioritize(BuildableType type, double weight, double support) {
    BuildPlan bp = findBuildPlan(type);
    if (bp == null) {
        buildPlans.add(new BuildPlan(type, weight, support));
        return true;
    }
    if (bp.weight * bp.support < weight * support) {
        bp.weight = weight;
        bp.support = support;
        return true;
    }
    return false;
}
