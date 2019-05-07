/**
     * Returns the stance towards a given player. <BR>
     * <BR>
     * One of: WAR, CEASE_FIRE, PEACE and ALLIANCE.
     *
     * @param player The <code>Player</code>.
     * @return The stance.
     */
public Stance getStance(Player player) {
    return (player == null || stance.get(player.getId()) == null) ? Stance.UNCONTACTED : stance.get(player.getId());
}
