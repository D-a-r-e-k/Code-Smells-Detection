/******************************************************************************/
/**
 * Calculates the barycenter of a graph, given by a list. This calculation is
 * done by summing the coordinates and dividing them with the number of 
 * coordinates.
 * 
 * @param list List of CellView's
 * @return Position of the barycenter
 */
private Point2D.Double computeBarycenter(ArrayList list) {
    double sumX = 0.0;
    double sumY = 0.0;
    for (int i = 0; i < list.size(); i++) {
        CellView view = (CellView) list.get(i);
        initPosition(view);
        Point2D.Double pos = getPosition(view);
        sumX += pos.x;
        sumY += pos.y;
    }
    return new Point2D.Double(sumX / (list.size()), sumY / (list.size()));
}
