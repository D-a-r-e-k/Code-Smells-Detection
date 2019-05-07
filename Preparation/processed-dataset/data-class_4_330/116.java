/**
     * Gets the number of King's land units.
     * @return The number of units
     */
public int getNumberOfKingLandUnits() {
    int n = 0;
    for (Unit unit : getUnits()) {
        if (unit.hasAbility("model.ability.refUnit") && !unit.isNaval()) {
            n++;
        }
    }
    return n;
}
