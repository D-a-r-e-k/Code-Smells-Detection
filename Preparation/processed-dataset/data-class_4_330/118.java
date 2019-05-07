/**
     * Gets the carrier units that can carry the supplied unit, if one exists.
     *
     * @param unit The <code>Unit</code> to carry.
     * @return A list of suitable carriers.
     */
public List<Unit> getCarriersForUnit(Unit unit) {
    List<Unit> units = new ArrayList<Unit>();
    for (Unit u : getUnits()) {
        if (u.couldCarry(unit))
            units.add(u);
    }
    return units;
}
