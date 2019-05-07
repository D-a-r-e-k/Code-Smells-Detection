/**
     * Gets the fastest naval unit type in this specification.
     *
     * @return The fastest naval unit type.
     */
public UnitType getFastestNavalUnitType() {
    if (cachedFastestNavalUnitType == null) {
        int bestValue = -1;
        for (UnitType t : unitTypeList) {
            if (t.isNaval() && t.getMovement() > bestValue) {
                bestValue = t.getMovement();
                cachedFastestNavalUnitType = t;
            }
        }
    }
    return cachedFastestNavalUnitType;
}
