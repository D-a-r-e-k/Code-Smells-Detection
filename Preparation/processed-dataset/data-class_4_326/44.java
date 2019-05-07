/**
     * Refreshes the screen at the specified Tile.
     *
     * @param t The tile to refresh.
     */
public void refreshTile(Tile t) {
    if (t.getX() >= 0 && t.getY() >= 0) {
        canvas.repaint(mapViewer.getTileBounds(t));
    }
}
