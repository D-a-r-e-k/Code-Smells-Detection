/**
     * Returns the <code>IndianSettlement</code> with the given name.
     *
     * @param name The name of the <code>IndianSettlement</code>.
     * @return The <code>IndianSettlement</code> or <code>null</code> if this player
     *         does not have a <code>IndianSettlement</code> with the specified name.
     */
public IndianSettlement getIndianSettlement(String name) {
    for (IndianSettlement settlement : getIndianSettlements()) {
        if (settlement.getName().equals(name)) {
            return settlement;
        }
    }
    return null;
}
