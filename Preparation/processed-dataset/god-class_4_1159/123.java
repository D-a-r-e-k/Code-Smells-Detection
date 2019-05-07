/**
     * Sets a new going-to unit.
     *
     * @param unit A <code>Unit</code> to make the next one to be active.
     * @return True if the operation succeeded.
     */
public boolean setNextGoingToUnit(Unit unit) {
    return nextGoingToUnitIterator.setNext(unit);
}
