/**
     * Set this player as having made initial contact with another player.
     * Always start with PEACE, which can go downhill fast.
     *
     * @param player1 a <code>Player</code> value
     * @param player2 a <code>Player</code> value
     */
public static void makeContact(Player player1, Player player2) {
    player1.stance.put(player2.getId(), Stance.PEACE);
    player2.stance.put(player1.getId(), Stance.PEACE);
    player1.setTension(player2, new Tension(Tension.TENSION_MIN));
    player2.setTension(player1, new Tension(Tension.TENSION_MIN));
}
