/**
     * Trooper's equipment dies when they do.
     */
@Override
public boolean hasHittableCriticals(int loc) {
    if (LOC_SQUAD == loc) {
        return false;
    }
    return super.hasHittableCriticals(loc);
}
