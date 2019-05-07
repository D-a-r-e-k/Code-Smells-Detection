/**
     * Gets a new active unit.
     *
     * @return A <code>Unit</code> that can be made active.
     */
public Unit getNextActiveUnit() {
    return nextActiveUnitIterator.next();
}
