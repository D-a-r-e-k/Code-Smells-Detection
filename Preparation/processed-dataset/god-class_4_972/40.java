/******************************************************************************/
/**
 * Recalculates the position of a cluster. The position of a cluster is defined
 * by the barycenter of the clustered vertices.
 * 
 * @param cluster Cell, that has to be a cluster, should be repositioned.
 */
protected void computeClusterPosition(VertexView cluster) {
    ArrayList clusteredVertices = (ArrayList) cluster.getAttributes().get(KEY_CLUSTERED_VERTICES);
    Point2D.Double clusterPos = computeBarycenter(clusteredVertices);
    cluster.getAttributes().put(KEY_POSITION, clusterPos);
}
