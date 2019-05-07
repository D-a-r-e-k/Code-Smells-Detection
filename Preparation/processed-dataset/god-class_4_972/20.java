/******************************************************************************/
/**
 * Applies the changes to the Cells. This means, that all temporary stored
 * positions are applied to all cells in {@link #applyCellList}
 */
private boolean setNewCoordinates(JGraph jgraph) {
    Map viewMap = new Hashtable();
    for (int i = 0; i < cellList.size(); i++) {
        Point2D.Double pos = getPosition(i, cellList);
        Rectangle2D r = ((CellView) cellList.get(i)).getBounds();
        r.setFrame(pos.getX() - (r.getWidth() / 2.0), pos.getY() - (r.getHeight() / 2.0), r.getWidth(), r.getHeight());
        Object cell = ((CellView) cellList.get(i)).getCell();
        Map map = new Hashtable();
        GraphConstants.setBounds(map, r);
        viewMap.put(cell, map);
    }
    jgraph.getGraphLayoutCache().edit(viewMap, null, null, null);
    return false;
}
