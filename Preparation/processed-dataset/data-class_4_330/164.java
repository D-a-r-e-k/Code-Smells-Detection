/**
     * Returns whether this player has met with any Europeans at all.
     *
     * @return <code>true</code> if this <code>Player</code> has contacted
     *         any Europeans.
     */
public boolean hasContactedEuropeans() {
    for (Player other : getGame().getLiveEuropeanPlayers()) {
        if (other != this && hasContacted(other)) {
            return true;
        }
    }
    return false;
}
