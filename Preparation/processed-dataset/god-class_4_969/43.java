/******************************************************************************/
/******************** CLUSTERING METHODS **************************************/
/******************************************************************************/
/**
 * Clusters a graph. Cells, contained in {@link #cellList} and not contained
 * in {@link #applyCellList} are clustered by this short algorithm. The
 * algorithm first tries to identify how many cells it should cluster. This
 * is calculated by subtracting the size of {@link #applyCellList} from
 * the size of {@link #cellList} and dividing the result by the 
 * {@link #clusteringFactor}. In the next step, the identified number of
 * clusters are created, and their position is initialised by random. Then
 * every clusterable cell is added to the cluster where the distance of the
 * vertex and the cluster is minimal. After adding a cell, the clusters position
 * is recalculated. Finishing this step, the algorithm tries to minimize the
 * number of clusters, by sorting the clustered vertices, if there is another
 * cluster, that distance is shorter than the distance to the cluster, the
 * vertice is actually in. This can happen, because by moving vertices into the
 * clusters, the position of the clusters are changed. The minimization runs
 * until no vertice can be moved anymore. empty clusters are removed and finaly 
 * the clusters are added to {@link #applyCellList}, because they should move
 * while the upcoming next calculations. That move can later be retrieved by
 * subtracting the attributes {@link #KEY_POSITION} and 
 * {@link #KEY_CLUSTER_INIT_POSITION}.
 * 
 * @see #declusterGraph()
 */
protected void clusterGraph() {
    //initialisation 
    int maxClusters = Math.max((int) ((cellList.size() - applyCellList.size()) / clusteringFactor), 2);
    if (cellList.size() <= 1) {
        System.out.println("cellList.size() <= 1");
        return;
    }
    ArrayList clusterList = new ArrayList();
    ArrayList cellsToCluster = new ArrayList();
    //identifying all cells, that are clusterable 
    for (int i = 0; i < cellList.size(); i++) if (!applyCellList.contains(cellList.get(i)))
        cellsToCluster.add(cellList.get(i));
    //initialize clusters 
    VertexView[] clusters = new VertexView[maxClusters];
    Rectangle boundingBox = getBoundingBox();
    for (int i = 0; i < clusters.length; i++) {
        clusters[i] = new VertexView(null);
        Map attributes = clusters[i].getAttributes();
        attributes.put(KEY_IS_CLUSTER, "true");
        attributes.put(KEY_POSITION, new Point2D.Double(Math.random() * boundingBox.width, Math.random() * boundingBox.height));
        clusterList.add(clusters[i]);
    }
    //cluster all available cells 
    for (int i = 0; i < cellsToCluster.size(); i++) {
        VertexView cell = (VertexView) cellsToCluster.get(i);
        Point2D.Double cellPos = getPosition(cell);
        int clusterID = 0;
        Point2D.Double clusterPos = getPosition((CellView) clusterList.get(0));
        double minDistance = MathExtensions.getEuclideanDistance(cellPos, clusterPos);
        //search for nearest cluster 
        for (int j = 1; j < clusterList.size(); j++) {
            clusterPos = getPosition((VertexView) clusterList.get(j));
            double distance = MathExtensions.getEuclideanDistance(cellPos, clusterPos);
            if (minDistance > distance) {
                minDistance = distance;
                clusterID = j;
            }
        }
        VertexView cluster = (VertexView) clusterList.get(clusterID);
        moveVerticeToCluster(cell, cluster);
    }
    //initialization done 
    //sorting the clustered vertices. if a vertice is nearer to a clusters 
    //barycenter then to it's own clusters barycenter the vertice is moved 
    //to that cluster. The coordinates of both clusters are recalculated. 
    //this is done, until nothing could be done better. 
    boolean couldMakeItBetter = false;
    do {
        couldMakeItBetter = false;
        for (int i = 0; i < cellsToCluster.size(); i++) {
            VertexView cell = (VertexView) cellsToCluster.get(i);
            VertexView oldCluster = (VertexView) cell.getAttributes().get(KEY_CLUSTER);
            Point2D.Double cellPos = getPosition(cell);
            Point2D.Double clusterPos = getPosition(oldCluster);
            double distance = MathExtensions.getEuclideanDistance(cellPos, clusterPos);
            for (int j = 0; j < clusterList.size(); j++) {
                VertexView cluster = (VertexView) clusterList.get(j);
                if (cluster != oldCluster) {
                    clusterPos = getPosition(cluster);
                    double newDistance = MathExtensions.getEuclideanDistance(cellPos, clusterPos);
                    if (newDistance < distance) {
                        moveVerticeToCluster(cell, cluster);
                        couldMakeItBetter = true;
                        break;
                    }
                }
            }
        }
    } while (couldMakeItBetter);
    //empty clusters are removed 
    for (int i = 0; i < clusterList.size(); i++) {
        if (!((VertexView) clusterList.get(i)).getAttributes().containsKey(KEY_CLUSTERED_VERTICES)) {
            clusterList.remove(i--);
        } else if (((ArrayList) ((VertexView) clusterList.get(i)).getAttributes().get(KEY_CLUSTERED_VERTICES)).size() == 0) {
            clusterList.remove(i--);
        }
    }
    //remove clustered vertices from cellList 
    for (int i = 0; i < cellsToCluster.size(); i++) cellList.remove(cellsToCluster.get(i));
    //adding clusters to applyCellList and cellList 
    for (int i = 0; i < clusterList.size(); i++) {
        applyCellList.add(clusterList.get(i));
        cellList.add(clusterList.get(i));
    }
    //storing a copy of position, to move vertices while declustering 
    for (int i = 0; i < clusterList.size(); i++) {
        VertexView cluster = (VertexView) clusterList.get(i);
        Map attribs = cluster.getAttributes();
        Point2D.Double clusterPos = (Point2D.Double) attribs.get(KEY_POSITION);
        attribs.put(KEY_CLUSTER_INIT_POSITION, new Point2D.Double(clusterPos.x, clusterPos.y));
    }
    for (int i = 0; i < clusterList.size(); i++) {
        VertexView cluster = (VertexView) clusterList.get(i);
        cluster.setCachedBounds(getBoundingBox((ArrayList) cluster.getAttributes().get(KEY_CLUSTERED_VERTICES)));
    }
}
