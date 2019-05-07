/******************************************************************************/
/**
 * Extracts the Positions of all cells into a array of Positions.
 * @return Array that represents the Positions of the Cells in 
 * {@link #applyCellList}.
 */
private Point2D.Double[] getConfig() {
    Point2D.Double[] config = new Point2D.Double[applyCellList.size()];
    for (int i = 0; i < applyCellList.size(); i++) {
        Point2D.Double pos = getPosition((CellView) applyCellList.get(i));
        config[i] = new Point2D.Double(pos.x, pos.y);
    }
    return config;
}
