/**
     * Get the player death state.
     * This is indeed identical to isDead(), but is needed for partial
     * updates to complement the setDead() function.
     *
     * @return True if this <code>Player</code> is dead.
     */
public boolean getDead() {
    return dead;
}
