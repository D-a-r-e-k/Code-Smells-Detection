static HsqlList resolveColumnSet(RangeVariable[] rangeVars, int rangeCount, HsqlList sourceSet, HsqlList targetSet) {
    if (sourceSet == null) {
        return targetSet;
    }
    for (int i = 0; i < sourceSet.size(); i++) {
        Expression e = (Expression) sourceSet.get(i);
        targetSet = e.resolveColumnReferences(rangeVars, rangeCount, targetSet, true);
    }
    return targetSet;
}
