/**
     * Checks if the given <code>Player</code> equals this object.
     *
     * @param o The <code>Player</code> to compare against this object.
     * @return <i>true</i> if the two <code>Player</code> are equal and none
     *         of both have <code>nation == null</code> and <i>false</i>
     *         otherwise.
     */
public boolean equals(Player o) {
    if (o == null) {
        return false;
    } else if (getId() == null || o.getId() == null) {
        // This only happens in the client code with the virtual "enemy 
        // privateer" player 
        // This special player is not properly associated to the Game and 
        // therefore has no ID 
        // TODO: remove this hack when the virtual "enemy privateer" player 
        // is better implemented 
        return false;
    } else {
        return getId().equals(o.getId());
    }
}
