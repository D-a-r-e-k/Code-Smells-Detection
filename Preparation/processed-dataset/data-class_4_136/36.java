/******************************************************************************/
/**
 * Recursiv method for adding all relatives whithin a given pathlength away
 * from the given array of Vertices to {@link #applyCellList}.
 * 
 * @param vertexList List of Vertices, which relatives might be added.
 * @param depth pathlength, the vertices adding to {@link #applyCellList} could
 * be away from the given array's vertices.
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
            if (!applyCellList.contains(relatives.get(j)))
                applyCellList.add(relatives.get(j));
            if (!cellList.contains(relatives.get(j)))
                cellList.add(relatives.get(j));
            relativeList[j] = (VertexView) relatives.get(j);
        }
        addRelativesToList(relativeList, depth - 1);
    }
}
