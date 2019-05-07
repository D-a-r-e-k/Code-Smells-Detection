/******************************************************************************/
/**
 * Computes the bounding box of the whole graph. The result is a Rectangle, 
 * parallel to the X- and Y-axises of the drawing system, closing about the 
 * whole graph.
 * @return Rectangle, that contains the whole graph.
 * @see #getBoundingBox(ArrayList) 
 */
private Rectangle getBoundingBox() {
    return getBoundingBox(cellList);
}
