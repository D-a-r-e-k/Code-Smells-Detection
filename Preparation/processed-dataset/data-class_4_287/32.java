int[] getLimits(Session session, int maxRows) {
    int skipRows = 0;
    int limitRows = Integer.MAX_VALUE;
    int limitFetch = Integer.MAX_VALUE;
    boolean hasLimits = false;
    if (sortAndSlice.hasLimit()) {
        Integer value = (Integer) sortAndSlice.limitCondition.getLeftNode().getValue(session);
        if (value == null || value.intValue() < 0) {
            throw Error.error(ErrorCode.X_2201X);
        }
        skipRows = value.intValue();
        hasLimits = skipRows != 0;
        if (sortAndSlice.limitCondition.getRightNode() != null) {
            value = (Integer) sortAndSlice.limitCondition.getRightNode().getValue(session);
            if (value == null || value.intValue() <= 0) {
                throw Error.error(ErrorCode.X_2201W);
            }
            if (value.intValue() == 0) {
                limitRows = Integer.MAX_VALUE;
            } else {
                limitRows = value.intValue();
                hasLimits = true;
            }
        }
    }
    if (maxRows != 0) {
        if (maxRows < limitRows) {
            limitRows = maxRows;
        }
        hasLimits = true;
    }
    if (hasLimits && simpleLimit && (!sortAndSlice.hasOrder() || sortAndSlice.skipSort) && (!sortAndSlice.hasLimit() || sortAndSlice.skipFullResult)) {
        if (limitFetch - skipRows > limitRows) {
            limitFetch = skipRows + limitRows;
        }
    }
    return hasLimits ? new int[] { skipRows, limitRows, limitFetch } : defaultLimits;
}
