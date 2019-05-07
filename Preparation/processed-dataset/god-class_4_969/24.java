/******************************************************************************/
/**
 * Costfunction. This is a extension to the original Algorithm. This method
 * evaluates the distances between cells. When the distance is lower than 
 * {@link #minDistance} or the cells are overlapping the costs from this 
 * function increase.
 * 
 * @param lambda Normalizing value for this function. Increasing this value
 * brings up less overlapping pairs of cells, by the expense of other 
 * aesthetics.
 * @return costs of this criterion. 
 */
private double getNodeDistance(double lambda) {
    double energy = 0.0;
    double radiusInc = 30.0;
    int overlapCount = 0;
    for (int i = 0; i < applyCellList.size(); i++) {
        Point2D.Double pos = (Point2D.Double) ((CellView) applyCellList.get(i)).getAttributes().get(KEY_POSITION);
        Rectangle2D vertex = ((CellView) applyCellList.get(i)).getBounds();
        for (int j = 0; j < cellList.size(); j++) {
            if (applyCellList.get(i) != cellList.get(j)) {
                Point2D.Double uPos = (Point2D.Double) ((CellView) cellList.get(j)).getAttributes().get(KEY_POSITION);
                Rectangle2D uVertex = ((CellView) cellList.get(j)).getBounds();
                double minDist = Math.max((2.0 * radiusInc) + (Math.max(vertex.getWidth(), vertex.getHeight()) / 2.0) + (Math.max(uVertex.getWidth(), uVertex.getHeight()) / 2.0), minDistance);
                double distance = Math.abs(pos.distance(uPos));
                //prevents from dividing with Zero 
                if (Math.abs(distance) < equalsNull)
                    distance = equalsNull;
                if (distance < minDist) {
                    energy += lambda / (distance * distance);
                    overlapCount++;
                }
            }
        }
    }
    return energy;
}
