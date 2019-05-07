/**
     * Adds a given settlement to this player's list of settlements.
     *
     * @param settlement The <code>Settlement</code> to add.
     */
public void addSettlement(Settlement settlement) {
    if (!hasSettlement(settlement)) {
        if (settlement.getOwner() != this) {
            throw new IllegalStateException("Player does not own settlement.");
        }
        settlements.add(settlement);
    }
}
