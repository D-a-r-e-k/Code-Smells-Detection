/******************************************************************************/
/**
 * After the calculation of the new Layout for a graph, the cells of the graph
 * are positioned somewhere on the drawing space. They even might have negative
 * coordinates. To prevent from this, this method is called, everytime before
 * {@link #applyChanges()} is called. This method moves the whole graph to the
 * upper left corner. No cell will have negative x- or y-coordinates. 
 */
private void moveGraphToNW() {
    Point2D.Double firstPos = getPosition((CellView) cellList.get(0));
    double minX = firstPos.x;
    double minY = firstPos.y;
    double maxX = minX;
    double maxY = minY;
    for (int i = 0; i < cellList.size(); i++) {
        CellView view = (CellView) cellList.get(i);
        Point2D.Double viewPos = getPosition((CellView) cellList.get(i));
        Rectangle2D viewBounds = view.getAttributes().createRect(view.getBounds());
        if (viewPos.getX() < minX) {
            minX = viewPos.getX();
        } else if (viewPos.getX() + viewBounds.getWidth() > maxX) {
            maxX = viewPos.getX() + viewBounds.getWidth();
        }
        if (viewPos.getY() < minY) {
            minY = viewPos.getY();
        } else if (viewPos.getY() + viewBounds.getHeight() > maxY) {
            maxY = viewPos.getY() + viewBounds.getHeight();
        }
    }
    minX -= 50;
    minY -= 50;
    for (int i = 0; i < cellList.size(); i++) {
        CellView view = (CellView) cellList.get(i);
        Point2D.Double pos = getPosition(view);
        setPosition(view, new Point2D.Double(pos.x - minX, pos.y - minY));
    }
}
