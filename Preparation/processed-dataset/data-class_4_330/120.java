/**
     * Sets a new active unit.
     *
     * @param unit A <code>Unit</code> to make the next one to be active.
     * @return True if the operation succeeded.
     */
public boolean setNextActiveUnit(Unit unit) {
    return nextActiveUnitIterator.setNext(unit);
}
