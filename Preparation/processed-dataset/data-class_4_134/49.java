/******************************************************************************/
/**
 * Computes the bounding box of the graph in the given list of CellViews. 
 * The result is a Rectangle, parallel to the X- and Y-axises of the drawing 
 * system, closing about the graph in the given list.
 * 
 * @param verticeList List containing the CellViews, the bounding box is of
 * interest.
 * @return Rectangle, that contains the whole graph, linked in the given list. 
 */
private Rectangle getBoundingBox(ArrayList verticeList) {
    if (verticeList.size() > 0) {
        Point2D.Double vertexPos = getPosition((VertexView) verticeList.get(0));
        Rectangle2D vertexSize = ((CellView) verticeList.get(0)).getBounds();
        double minX = vertexPos.getX();
        double minY = vertexPos.getX();
        double maxX = vertexPos.getX() + vertexSize.getWidth();
        double maxY = vertexPos.getX() + vertexSize.getHeight();
        for (int i = 1; i < verticeList.size(); i++) {
            vertexPos = getPosition((VertexView) verticeList.get(i));
            vertexSize = ((CellView) verticeList.get(i)).getBounds();
            if (minX > vertexPos.getX())
                minX = vertexPos.getX();
            if (minY > vertexPos.getY())
                minY = vertexPos.getY();
            if (maxX < vertexPos.getX() + vertexSize.getWidth())
                maxX = vertexPos.getX() + vertexSize.getWidth();
            if (maxY < vertexPos.getY() + vertexSize.getHeight())
                maxY = vertexPos.getY() + vertexSize.getHeight();
        }
        Rectangle boundingBox = new Rectangle((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
        return boundingBox;
    }
    return null;
}
