/**
     * Gets a list of all the IndianSettlements this player owns.
     * It is an error to call this on non-native players.
     *
     * @return The indian settlements this player owns.
     */
public List<IndianSettlement> getIndianSettlements() {
    List<IndianSettlement> indianSettlements = new ArrayList<IndianSettlement>();
    for (Settlement s : settlements) {
        if (s instanceof IndianSettlement) {
            indianSettlements.add((IndianSettlement) s);
        } else {
            throw new RuntimeException("getIndianSettlements found: " + s);
        }
    }
    return indianSettlements;
}
