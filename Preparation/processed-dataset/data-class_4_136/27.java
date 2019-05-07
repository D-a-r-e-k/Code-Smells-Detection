/******************************************************************************/
/**
 * Moves the graph to the upper left corner of the drawing space. This is done,
 * after a successfull run of the algorithm, to correct it's output.
 */
private void correctCoordinates() {
    Rectangle boundingBox = getBoundingBox();
    if (boundingBox != null) {
        for (int i = 0; i < cellList.size(); i++) {
            CellView view = (CellView) cellList.get(i);
            Point2D.Double pos = getPosition(view);
            Point2D.Double topLeftCorner = new Point2D.Double(pos.x + (view.getBounds().getWidth()) / 2, pos.y + (view.getBounds().getHeight()) / 2);
            Point2D.Double newPos = new Point2D.Double(topLeftCorner.x - boundingBox.getX(), topLeftCorner.y - boundingBox.getY());
            view.getAttributes().put(KEY_POSITION, newPos);
        }
    }
}
