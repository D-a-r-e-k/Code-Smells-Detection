public void activateGotoPath() {
    Unit unit = getActiveUnit();
    // Action should be disabled if there is no active unit, but make sure 
    if (unit == null)
        return;
    // Enter "goto mode" if not already activated; otherwise cancel it 
    if (mapViewer.isGotoStarted()) {
        mapViewer.stopGoto();
    } else {
        mapViewer.startGoto();
        // Draw the path to the current mouse position, if the 
        // mouse is over the screen; see also 
        // CanvaseMouseMotionListener 
        Point pt = canvas.getMousePosition();
        if (pt != null) {
            Tile tile = mapViewer.convertToMapTile(pt.x, pt.y);
            if (tile != null && unit.getTile() != tile) {
                mapViewer.setGotoPath(unit.findPath(tile));
            }
        }
    }
}
