/**
     * resolve tables and collect unresolved column expressions
     */
public HsqlList resolveColumnReferences(RangeVariable[] rangeVarArray, HsqlList unresolvedSet) {
    return resolveColumnReferences(rangeVarArray, rangeVarArray.length, unresolvedSet, true);
}
