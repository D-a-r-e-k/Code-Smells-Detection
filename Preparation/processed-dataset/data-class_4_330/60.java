/**
     * Can a tile be claimed to found a settlement on?
     * Almost the same as canClaimForSettlement but there is an extra
     * requirement that the tile be of a settleable type, and some
     * relaxations that allow free center tile acquisition
     *
     * @param tile The <code>Tile</code> to try to claim.
     * @return The reason why/not the tile can be claimed.
     */
public NoClaimReason canClaimToFoundSettlementReason(Tile tile) {
    NoClaimReason reason;
    return (!tile.getType().canSettle()) ? NoClaimReason.TERRAIN : ((reason = canClaimForSettlementReason(tile)) != NoClaimReason.NATIVES) ? reason : (canClaimFreeCenterTile(tile)) ? NoClaimReason.NONE : NoClaimReason.NATIVES;
}
