/******************************************************************************/
/**
 * Moves a vertice from the cluster, it is holded, to another cluster. This
 * implies that the vertice is removed from the old cluster and added to the 
 * new. After this, the positions of the old and the new cluster are 
 * recalculated.
 * 
 * @param vertice Vertex that should be moved
 * @param cluster Cluster the vertex should be moved
 */
protected void moveVerticeToCluster(VertexView vertice, VertexView cluster) {
    //adding vertice to new cluster 
    if (!cluster.getAttributes().containsKey(KEY_CLUSTERED_VERTICES))
        cluster.getAttributes().put(KEY_CLUSTERED_VERTICES, new ArrayList());
    ArrayList clusteredVertices = (ArrayList) cluster.getAttributes().get(KEY_CLUSTERED_VERTICES);
    clusteredVertices.add(vertice);
    //removing vertice from old cluster 
    if (vertice.getAttributes().containsKey(KEY_CLUSTER)) {
        VertexView oldCluster = (VertexView) vertice.getAttributes().get(KEY_CLUSTER);
        ArrayList list = (ArrayList) oldCluster.getAttributes().get(KEY_CLUSTERED_VERTICES);
        list.remove(vertice);
        computeClusterPosition(oldCluster);
    }
    //register cluster in vertice 
    vertice.getAttributes().put(KEY_CLUSTER, cluster);
    //reposition cluster 
    computeClusterPosition(cluster);
}
