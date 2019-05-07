/**
     * Returns whether this player has met with the <code>Player</code> if the
     * given <code>nation</code>.
     *
     * @param player The Player.
     * @return <code>true</code> if this <code>Player</code> has contacted
     *         the given nation.
     */
public boolean hasContacted(Player player) {
    return getStance(player) != Stance.UNCONTACTED;
}
