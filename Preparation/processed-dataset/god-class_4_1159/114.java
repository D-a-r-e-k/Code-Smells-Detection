/**
     * Gets an <code>Iterator</code> containing all the units this player
     * owns.
     *
     * @return The <code>Iterator</code>.
     * @see Unit
     */
public Iterator<Unit> getUnitIterator() {
    return units.values().iterator();
}
