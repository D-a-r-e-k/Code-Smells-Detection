/**
     * Sets the hostility against the given player.
     *
     * @param player The <code>Player</code>.
     * @param newTension The <code>Tension</code>.
     */
public void setTension(Player player, Tension newTension) {
    if (player == this || player == null) {
        return;
    }
    tension.put(player, newTension);
}
