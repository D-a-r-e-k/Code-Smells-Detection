/**
     * Can a tile be owned by this player?
     *
     * @param tile The <code>Tile</code> to consider.
     * @return True if the tile can be owned by this player.
     */
public boolean canOwnTile(Tile tile) {
    return canOwnTileReason(tile) == NoClaimReason.NONE;
}
