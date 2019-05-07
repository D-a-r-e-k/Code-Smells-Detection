/**
     * Can a tile be owned by this player?
     * This is a test of basic practicality and does not consider
     * the full complexity of tile ownership issues.
     *
     * @param tile The <code>Tile</code> to consider.
     * @return The reason why/not the tile can be owned by this player.
     */
private NoClaimReason canOwnTileReason(Tile tile) {
    for (Unit u : tile.getUnitList()) {
        Player owner = u.getOwner();
        if (owner == this || !owner.atWarWith(this))
            break;
        // Not hostile 
        // If the unit is military, the tile is held against us. 
        if (u.isOffensiveUnit())
            return NoClaimReason.OCCUPIED;
    }
    return (isEuropean()) ? ((tile.hasLostCityRumour()) ? NoClaimReason.RUMOUR : NoClaimReason.NONE) : ((tile.isLand()) ? NoClaimReason.NONE : NoClaimReason.WATER);
}
