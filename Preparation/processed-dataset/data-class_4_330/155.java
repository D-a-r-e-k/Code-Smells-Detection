/**
     * Gets the hostility this player has against the given player.
     *
     * @param player The <code>Player</code>.
     * @return An object representing the tension level.
     */
public Tension getTension(Player player) {
    if (player == null) {
        throw new IllegalStateException("Null player.");
    } else {
        Tension newTension = tension.get(player);
        if (newTension == null) {
            newTension = new Tension(Tension.TENSION_MIN);
        }
        tension.put(player, newTension);
        return newTension;
    }
}
