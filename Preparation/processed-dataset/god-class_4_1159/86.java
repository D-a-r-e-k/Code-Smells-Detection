/**
     * Checks if this <code>Player</code> can see the given <code>Tile</code>.
     * The <code>Tile</code> can be seen if it is in a {@link Unit}'s line of
     * sight.
     *
     * @param tile The given <code>Tile</code>.
     * @return <i>true</i> if the <code>Player</code> can see the given
     *         <code>Tile</code> and <i>false</i> otherwise.
     */
public boolean canSee(Tile tile) {
    if (tile == null)
        return false;
    do {
        synchronized (canSeeLock) {
            if (canSeeTiles != null) {
                return canSeeTiles[tile.getX()][tile.getY()];
            }
        }
    } while (resetCanSeeTiles());
    return false;
}
