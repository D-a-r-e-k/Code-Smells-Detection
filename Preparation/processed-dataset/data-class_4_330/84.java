/**
     * Sets the tiles within the given <code>Unit</code>'s line of sight to
     * be explored by this player.
     *
     * @param unit The <code>Unit</code>.
     * @see #setExplored(Tile)
     * @see #hasExplored
     */
public void setExplored(Unit unit) {
    if (getGame() == null || getGame().getMap() == null || unit == null || unit.getLocation() == null || unit.getTile() == null || isIndian()) {
        return;
    }
    invalidateCanSeeTiles();
}
