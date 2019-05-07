/**
     * The test for whether a tile can be freely claimed by a player
     * settlement (freely => not by purchase or stealing).  The rule
     * for the center tile is different, see below.
     *
     * The tile must be ownable by this player, settlement-free, and
     * either not currently owned, owned by this player and not by
     * another settlement that is using the tile, or owned by someone
     * else who does not want anything for it.  Got that?
     *
     * @param tile The <code>Tile</code> to try to claim.
     * @return The reason why/not the tile can be claimed.
     */
public NoClaimReason canClaimForSettlementReason(Tile tile) {
    int price;
    NoClaimReason reason = canOwnTileReason(tile);
    return (reason != NoClaimReason.NONE) ? reason : (tile.getSettlement() != null) ? NoClaimReason.SETTLEMENT : (tile.getOwner() == null) ? NoClaimReason.NONE : (tile.getOwner() == this) ? ((tile.isInUse()) ? NoClaimReason.WORKED : NoClaimReason.NONE) : ((price = getLandPrice(tile)) < 0) ? NoClaimReason.EUROPEANS : (price > 0) ? NoClaimReason.NATIVES : NoClaimReason.NONE;
}
