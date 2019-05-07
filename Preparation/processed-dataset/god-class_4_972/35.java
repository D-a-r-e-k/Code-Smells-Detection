/******************************************************************************/
/**
 * Method for the process of layout update. Adds inserted cells to 
 * {@link #applyCellList} and some of their neighbors. Adding of neighbors is
 * deceided by {@link #layoutUpdateMethod}. If a method is choosen with
 * perimeter, than the inserted cells are counted, that have a position whithin
 * the basic radius around inserted cells. then a new radius is calculated by
 * multiplying the increasial radius with the number of inserted cells found and
 * adding it to the basic radius. Then all cells, previously layouted whithin
 * this radius are also added to {@link #applyCellList}. After this, cells
 * within a given pathlength smaller than {@link #recursionDepth} are added
 * to {@link #applyCellList} too.
 * 
 * @param vertexList List of all inserted Vertices. 
 */
public void addApplyableVertices(VertexView[] vertexList) {
    for (int i = 0; i < vertexList.length; i++) {
        if (!applyCellList.contains(vertexList[i]))
            applyCellList.add(vertexList[i]);
        if (!cellList.contains(vertexList[i]))
            cellList.add(vertexList[i]);
    }
    if (GEMLayoutSettings.KEY_LAYOUT_UPDATE_METHOD_PERIMETERS.equals(layoutUpdateMethod)) {
        for (int i = 0; i < vertexList.length; i++) {
            double perimeterSize = perimeterInitSize;
            Point2D.Double pos = getPosition(vertexList[i]);
            for (int j = 0; j < vertexList.length; j++) {
                if (i != j) {
                    Point2D.Double oPos = getPosition(vertexList[j]);
                    if (Math.abs(pos.distance(oPos)) < (perimeterInitSize / 2.0))
                        perimeterSize += perimeterSizeInc;
                }
            }
            for (int j = 0; j < cellList.size(); j++) {
                Point2D.Double uPos = getPosition(j, cellList);
                if (Math.abs(pos.distance(uPos)) < (perimeterSize / 2.0) && !applyCellList.contains(cellList.get(j)))
                    applyCellList.add(cellList.get(j));
            }
        }
        vertexList = new VertexView[applyCellList.size()];
        for (int i = 0; i < applyCellList.size(); i++) vertexList[i] = (VertexView) applyCellList.get(i);
    }
    if (recursionDepth > 0)
        addRelativesToList(vertexList, recursionDepth);
}
