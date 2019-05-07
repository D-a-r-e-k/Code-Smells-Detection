/**
     * Gets the <code>Player</code> controlling the "Royal Expeditionary
     * Force" for this player.
     *
     * @return The player, or <code>null</code> if this player does not have a
     *         royal expeditionary force.
     */
public Player getREFPlayer() {
    Nation ref = getNation().getRefNation();
    return (ref == null) ? null : getGame().getPlayer(ref.getId());
}
