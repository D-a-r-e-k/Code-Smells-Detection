/**
     * Removes the given settlement from this player's list of settlements.
     *
     * @param settlement The <code>Settlement</code> to remove.
     * @return True if the settlement was removed.
     */
public boolean removeSettlement(Settlement settlement) {
    return settlements.remove(settlement);
}
