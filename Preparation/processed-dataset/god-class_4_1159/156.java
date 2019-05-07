/**
     * Removes all tension with respect to a given player.  Used when a
     * player leaves the game.
     *
     * @param player The <code>Player</code> to remove tension for.
     */
public void removeTension(Player player) {
    if (player != null)
        tension.remove(player);
}
