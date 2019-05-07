/**
     * Checks if this player is a "royal expeditionary force.
     *
     * @return <code>true</code> is the given nation is a royal expeditionary
     *         force and <code>false</code> otherwise.
     */
public boolean isREF() {
    return nationType != null && nationType.isREF();
}
