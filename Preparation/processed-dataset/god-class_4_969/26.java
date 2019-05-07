/******************************************************************************/
/**
 * Returns all Edges that are connected with cells, member of 
 * {@link #applyCellList}, except the edges connected the the given cell.
 * @param except Edges connected to this cell are not of interest
 * @return List of all interesting Edges
 */
private ArrayList getRelevantEdges(CellView except) {
    ArrayList relevantEdgeList = new ArrayList();
    for (int i = 0; i < edgeList.size(); i++) {
        CellView view = ((EdgeView) edgeList.get(i)).getSource().getParentView();
        if (view != except && applyCellList.contains(view)) {
            relevantEdgeList.add(edgeList.get(i));
        } else {
            view = ((EdgeView) edgeList.get(i)).getTarget().getParentView();
            if (view != except && applyCellList.contains(view)) {
                relevantEdgeList.add(edgeList.get(i));
            }
        }
    }
    return relevantEdgeList;
}
