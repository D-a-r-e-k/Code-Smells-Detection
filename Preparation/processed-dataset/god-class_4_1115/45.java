void setMergeability() {
    if (isGrouped || isDistinctSelect) {
        isMergeable = false;
        return;
    }
    if (sortAndSlice.hasLimit() || sortAndSlice.hasOrder()) {
        isMergeable = false;
        return;
    }
    if (rangeVariables.length != 1) {
        isMergeable = false;
        return;
    }
}
