/**
     * Returns a list of all IndianSettlements this player owns that have
     * missions, optionally owned by a specific player.
     *
     * @param other If non-null, collect only missions established by
     *     this <code>Player</code>
     * @return The settlements this player owns with the specified
     *     mission type.
     */
public List<IndianSettlement> getIndianSettlementsWithMission(Player other) {
    List<IndianSettlement> indianSettlements = new ArrayList<IndianSettlement>();
    for (Settlement s : settlements) {
        Unit missionary;
        if (s instanceof IndianSettlement && (missionary = ((IndianSettlement) s).getMissionary()) != null && (other == null || missionary.getOwner() == other)) {
            indianSettlements.add((IndianSettlement) s);
        }
    }
    return indianSettlements;
}
