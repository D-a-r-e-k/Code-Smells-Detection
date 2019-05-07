/**
     * Is this player at war with the specified one.
     *
     * @param player The <code>Player</code> to check.
     * @return True if the players are at war.
     */
public boolean atWarWith(Player player) {
    return getStance(player) == Stance.WAR;
}
