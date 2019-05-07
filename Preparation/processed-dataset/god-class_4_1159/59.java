/**
     * Can a tile be claimed to found a settlement on?
     *
     * @param tile The <code>Tile</code> to try to claim.
     * @return True if the tile can be claimed to found a settlement.
     */
public boolean canClaimToFoundSettlement(Tile tile) {
    return canClaimToFoundSettlementReason(tile) == NoClaimReason.NONE;
}
