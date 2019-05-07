/**
     * Adds to the current amount of liberty this player has.
     *
     * @param amount The additional amount of liberty.
     */
public void incrementLiberty(int amount) {
    setLiberty(Math.max(0, getLiberty() + amount));
    if (playerType == PlayerType.REBEL) {
        interventionBells += amount;
    }
}
