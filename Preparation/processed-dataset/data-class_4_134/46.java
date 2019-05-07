/******************************************************************************/
/**
 * Moves all clusters from {@link #cellList} and {@link #applyCellList}, 
 * extracts their clustered vertices and adds them to {@link #cellList}. While
 * doing this, it repositions the clustered vertices with the move, the cluster
 * has made during the calculation.
 * 
 * @see #clusterGraph() 
 */
protected void declusterGraph() {
    if (cellList.size() <= 1)
        return;
    //first collecting all clusters from applyCellList 
    ArrayList clusterList = new ArrayList();
    for (int i = 0; i < cellList.size(); i++) {
        VertexView cell = ((VertexView) cellList.get(i));
        if (isCluster(cell))
            clusterList.add(cell);
    }
    if (clusterList.size() == 0)
        return;
    //cleaning up the cell lists 
    for (int i = 0; i < clusterList.size(); i++) {
        cellList.remove(clusterList.get(i));
        applyCellList.remove(clusterList.get(i));
    }
    //repositioning and extracting vertices to cellList  
    for (int i = 0; i < clusterList.size(); i++) {
        VertexView cluster = (VertexView) clusterList.get(i);
        Map attribs = cluster.getAttributes();
        Point2D.Double newClusterPos = getPosition(cluster);
        Point2D.Double oldClusterPos = (Point2D.Double) attribs.get(KEY_CLUSTER_INIT_POSITION);
        //calculating move, cluster has made during his existance 
        Point2D.Double move = new Point2D.Double(newClusterPos.x - oldClusterPos.x, newClusterPos.y - oldClusterPos.y);
        ArrayList vertexList = (ArrayList) attribs.get(KEY_CLUSTERED_VERTICES);
        //applying move to clustered vertices 
        for (int j = 0; j < vertexList.size(); j++) {
            VertexView cell = (VertexView) vertexList.get(j);
            Point2D.Double cellPos = getPosition(cell);
            Point2D.Double newCellPos = new Point2D.Double(cellPos.x + move.x, cellPos.y + move.y);
            cell.getAttributes().put(KEY_POSITION, newCellPos);
            //refilling clustered vertices in cellList 
            cellList.add(cell);
        }
    }
}
