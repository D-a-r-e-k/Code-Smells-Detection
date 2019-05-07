/**
     * Returns the sum of units currently working in the colonies of
     * this player.
     *
     * @return Sum of units currently working in the colonies.
     */
public int getColoniesPopulation() {
    int i = 0;
    for (Colony c : getColonies()) {
        i += c.getUnitCount();
    }
    return i;
}
