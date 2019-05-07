/**
     * Can a tile be acquired from its owners and used for an improvement?
     * Slightly weakens canClaimForImprovement to allow for purchase
     * and/or stealing.
     *
     * @param tile The <code>Tile</code> to consider.
     * @return True if the tile ownership can be claimed.
     */
public boolean canAcquireForImprovement(Tile tile) {
    return canClaimForImprovement(tile) || getLandPrice(tile) > 0;
}
