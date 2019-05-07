private Result getSingleResult(Session session, int maxRows) {
    int[] limits = getLimits(session, maxRows);
    Result r = buildResult(session, limits[2]);
    RowSetNavigatorData navigator = (RowSetNavigatorData) r.getNavigator();
    if (isDistinctSelect) {
        navigator.removeDuplicates();
    }
    if (sortAndSlice.hasOrder()) {
        navigator.sortOrder();
    }
    if (limits != defaultLimits) {
        navigator.trim(limits[0], limits[1]);
    }
    return r;
}
