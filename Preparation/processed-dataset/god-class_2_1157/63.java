/**
     * Gets the fastest land unit type in this specification.
     *
     * @return The fastest land unit type.
     */
public UnitType getFastestLandUnitType() {
    if (cachedFastestLandUnitType == null) {
        int bestValue = -1;
        for (UnitType t : unitTypeList) {
            if (!t.isNaval() && t.getMovement() > bestValue) {
                bestValue = t.getMovement();
                cachedFastestLandUnitType = t;
            }
        }
    }
    return cachedFastestLandUnitType;
}
