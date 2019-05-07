/**
     * The second and third cases of buildOnNative land need to test
     * if the player has no settlements yet.  We can not just check
     * that the number of settlement is zero because by the time the
     * settlement is being placed and we are collecting the tiles to
     * claim, the settlement already exists and thus there will
     * already be one settlement--- so we have to check if that one
     * settlement is on the map yet.
     *
     * @return True if the player has no settlements (on the map) yet.
     */
private boolean hasZeroSettlements() {
    List<Settlement> settlements = getSettlements();
    return settlements.isEmpty() || (settlements.size() == 1 && settlements.get(0).getTile().getSettlement() == null);
}
