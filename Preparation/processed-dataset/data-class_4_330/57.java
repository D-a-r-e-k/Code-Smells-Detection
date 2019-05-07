/**
     * Checks if a tile can be claimed for use by a settlement.
     *
     * @param tile The <code>Tile</code> to try to claim.
     * @return True if the tile can be claimed to found a settlement.
     */
public boolean canClaimForSettlement(Tile tile) {
    return canClaimForSettlementReason(tile) == NoClaimReason.NONE;
}
