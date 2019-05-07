/******************************************************************************/
/**
 * Decides in a layout update process, what cells are member of 
 * {@link #applyCellList}. This depends on the gpConfiguration of the layout 
 * update method. First, regardless which layout update method was chosen, all
 * inserted cells, gained as parameter, are added. Then, when the perimeter
 * method is chosen, the cells are counted, which position is in the basic
 * perimeter radius around an inserted cell. That number multiplied with the
 * perimeter radius increase are added to the basic perimeter radius. Every
 * Cell, that was not inserted but is positioned in that radius, is added to
 * {@link #applyCellList}. After that, if perimeter method or neighbor method
 * is choosen, the relatives up to {@link #luRecursionDepth} away of the 
 * inserted cells are added to {@link #applyCellList}.
 * 
 * @param viewList Array of the inserted CellView's (includes EdgeView)
 * @see #graphChanged(GraphModelEvent) 
 */
private void getLayoutUpdateCells(CellView[] viewList) {
    //adds all inserted views 
    for (int i = 0; i < viewList.length; i++) {
        if (viewList[i] instanceof VertexView) {
            if (!applyCellList.contains(viewList[i]))
                applyCellList.add(viewList[i]);
            if (!cellList.contains(viewList[i]))
                cellList.add(viewList[i]);
        } else if (viewList[i] instanceof EdgeView && viewList[i] != null) {
            if (!edgeList.contains(viewList[i])) {
                edgeList.add(viewList[i]);
                System.out.println("edge added");
            }
        }
    }
    //now all vertices (old and new) are in cellList & all edges in edgeList 
    //adds all known cells in a perimeter 
    if (AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_METHOD_PERIMETER.equals(luMethod)) {
        //precalculation of perimeters 
        ArrayList perimeterList = new ArrayList();
        for (int i = 0; i < applyCellList.size(); i++) {
            VertexView vertex = (VertexView) applyCellList.get(i);
            Point2D.Double pos = (Point2D.Double) vertex.getAttributes().get(KEY_POSITION);
            int intersectionCount = 0;
            for (int j = 0; j < applyCellList.size(); j++) {
                if (i != j) {
                    VertexView uVertex = (VertexView) applyCellList.get(j);
                    Point2D.Double uPos = (Point2D.Double) uVertex.getAttributes().get(KEY_POSITION);
                    if (pos.distance(uPos) < luPerimeterRadius)
                        intersectionCount++;
                }
            }
            perimeterList.add(new Ellipse2D.Double(pos.x - (luPerimeterRadius + (intersectionCount * luPerimeterRadiusInc)), pos.y - (luPerimeterRadius + (intersectionCount * luPerimeterRadiusInc)), 2.0 * (luPerimeterRadius + (intersectionCount * luPerimeterRadiusInc)), 2.0 * (luPerimeterRadius + (intersectionCount * luPerimeterRadiusInc))));
        }
        //adding all members of cellList within a perimeter to applyCellList 
        for (int i = 0; i < cellList.size(); i++) {
            VertexView vertex = (VertexView) cellList.get(i);
            Point2D.Double pos = (Point2D.Double) vertex.getAttributes().get(KEY_POSITION);
            for (int j = 0; j < perimeterList.size(); j++) {
                Ellipse2D.Double perimeter = (Ellipse2D.Double) perimeterList.get(j);
                Point2D.Double center = new Point2D.Double(perimeter.getCenterX(), perimeter.getCenterY());
                double radius = perimeter.getCenterX() - perimeter.getX();
                if (center.distance(pos) < radius)
                    if (!applyCellList.contains(vertex))
                        applyCellList.add(vertex);
            }
        }
    }
    if (luRecursionDepth > 0) {
        int vertexCount = 0;
        for (int i = 0; i < viewList.length; i++) if (viewList[i] instanceof VertexView)
            vertexCount++;
        VertexView[] vertexList = new VertexView[vertexCount];
        vertexCount = 0;
        for (int i = 0; i < viewList.length; i++) if (viewList[i] instanceof VertexView)
            vertexList[vertexCount++] = (VertexView) viewList[i];
        addRelativesToList(vertexList, luRecursionDepth);
    }
}
