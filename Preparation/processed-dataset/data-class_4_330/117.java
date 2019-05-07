/**
     * Checks if this player has a single Man-of-War.
     * @return <code>true</code> if this player owns
     *      a single Man-of-War.
     */
public boolean hasManOfWar() {
    Iterator<Unit> it = getUnitIterator();
    while (it.hasNext()) {
        Unit unit = it.next();
        if ("model.unit.manOWar".equals(unit.getType().getId())) {
            return true;
        }
    }
    return false;
}
