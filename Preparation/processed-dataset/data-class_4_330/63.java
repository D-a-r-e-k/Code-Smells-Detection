/**
     * Can the ownership of this tile be claimed for the purposes of
     * making an improvement.  Quick test that does not handle the
     * curly case of tile transfer between colonies, or guarantee
     * success (natives may want to be paid), but just that success is
     * possible.
     *
     * @param tile The <code>Tile</code> to consider.
     *
     * @return True if the tile ownership can be claimed.
     */
public boolean canClaimForImprovement(Tile tile) {
    Player owner = tile.getOwner();
    return owner == null || owner == this || getLandPrice(tile) == 0;
}
