/**
     * Gets a new going-to unit.
     *
     * @return A <code>Unit</code> that can be made active.
     */
public Unit getNextGoingToUnit() {
    return nextGoingToUnitIterator.next();
}
