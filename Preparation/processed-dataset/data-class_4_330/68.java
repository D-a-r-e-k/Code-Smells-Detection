/**
     * Remove Unit.
     *
     * @param oldUnit an <code>Unit</code> value
     */
public void removeUnit(final Unit oldUnit) {
    if (oldUnit != null) {
        units.remove(oldUnit.getId());
        nextActiveUnitIterator.remove(oldUnit);
        nextGoingToUnitIterator.remove(oldUnit);
    }
}
