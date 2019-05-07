/**
     * Gets the current amount of liberty this <code>Player</code> has.
     *
     * @return This player's number of liberty earned towards the
     *     current Founding Father.
     */
public int getLiberty() {
    return (canHaveFoundingFathers()) ? liberty : 0;
}
