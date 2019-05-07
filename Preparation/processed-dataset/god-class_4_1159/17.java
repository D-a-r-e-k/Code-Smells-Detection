/**
     * Checks if this player owns the given <code>Settlement</code>.
     *
     * @param s The <code>Settlement</code>.
     * @return <code>true</code> if this <code>Player</code> owns the given
     *         <code>Settlement</code>.
     */
public boolean hasSettlement(Settlement s) {
    return settlements.contains(s);
}
