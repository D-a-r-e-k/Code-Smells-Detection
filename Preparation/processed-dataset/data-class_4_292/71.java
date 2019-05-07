/**
     * isCorrelated
     */
public boolean isCorrelated() {
    if (subQuery == null) {
        return false;
    }
    return subQuery.isCorrelated();
}
