/**
     * Returns whether this player has met with any natives at all.
     *
     * @return <code>true</code> if this <code>Player</code> has contacted
     *         any natives.
     */
public boolean hasContactedIndians() {
    for (Player other : getGame().getPlayers()) {
        if (other != this && !other.isDead() && other.isIndian() && hasContacted(other)) {
            return true;
        }
    }
    return false;
}
