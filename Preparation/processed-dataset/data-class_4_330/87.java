/**
     * Resets this player's "can see"-tiles. This is done by setting
     * all the tiles within each {@link Unit} and {@link Settlement}s
     * line of sight visible. The other tiles are made invisible.
     *
     * Use {@link #invalidateCanSeeTiles} whenever possible.
     * @return <code>true</code> if successful <code>false</code> otherwise
     */
private boolean resetCanSeeTiles() {
    Map map = getGame().getMap();
    if (map == null)
        return false;
    boolean[][] cST = makeCanSeeTiles(map);
    synchronized (canSeeLock) {
        canSeeTiles = cST;
    }
    return true;
}
