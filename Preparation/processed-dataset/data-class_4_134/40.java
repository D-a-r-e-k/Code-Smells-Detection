/******************************************************************************/
/**
 * Recursive method, to add relatives to {@link #applyCellList}, that are
 * maximal a given pathlength away of the views in the given Array.
 * 
 * @param vertexList Array of the VertexView's, which relatives should be
 * added to {@link #applyCellList}, if they are whithin a given pathlength
 * away of the VertexViews
 * @param depth Pathlength, relatives could be away of the VertexViews
 * @see #graphChanged(GraphModelEvent)
 */
private void addRelativesToList(VertexView[] vertexList, int depth) {
    if (vertexList == null)
        return;
    if (vertexList.length == 0)
        return;
    if (depth == 0)
        return;
    for (int i = 0; i < vertexList.length; i++) {
        ArrayList relatives = getRelatives(vertexList[i]);
        VertexView[] relativeList = new VertexView[relatives.size()];
        for (int j = 0; j < relatives.size(); j++) {
            if (!applyCellList.contains(relatives.get(j))) {
                applyCellList.add(relatives.get(j));
            }
            if (!cellList.contains(relatives.get(j)))
                cellList.add(relatives.get(j));
            relativeList[j] = (VertexView) relatives.get(j);
        }
        addRelativesToList(relativeList, depth - 1);
    }
}
