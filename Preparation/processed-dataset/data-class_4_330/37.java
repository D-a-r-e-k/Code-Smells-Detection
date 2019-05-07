/**
     * Gets a new name for a unit.
     * Currently only names naval units, not specific to type.
     * TODO: specific names for types.
     *
     * @param type The <code>UnitType</code> to choose a name for.
     * @param random A pseudo-random number source.
     * @return A name for the unit, or null if not available.
     */
public String getUnitName(UnitType type, Random random) {
    String name;
    if (!type.isNaval())
        return null;
    // Collect all the names of existing naval units. 
    List<String> navalNames = new ArrayList<String>();
    for (Unit u : getUnits()) {
        if (u.isNaval() && u.getName() != null) {
            navalNames.add(u.getName());
        }
    }
    // Find a new name in the installed ship names if possible. 
    if (shipNames == null)
        initializeShipNames(random);
    while (!shipNames.isEmpty()) {
        name = shipNames.remove(0);
        if (!navalNames.contains(name))
            return name;
    }
    // Fallback method 
    if (shipFallback != null) {
        final String base = shipFallback + "-";
        int i = 0;
        while (navalNames.contains(name = base + Integer.toString(i))) i++;
        return name;
    }
    return null;
}
