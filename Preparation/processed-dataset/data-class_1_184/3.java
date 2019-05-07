@SuppressWarnings("unchecked")
private void addEdgesToGraph(G graph, Collection<EdgeMetadata> metadata, Map<String, V> idToVertexMap) throws GraphIOException {
    for (EdgeMetadata emd : metadata) {
        // Get the edge out of the metadata 
        E edge = (E) emd.getEdge();
        // Get the verticies. 
        V source = idToVertexMap.get(emd.getSource());
        V target = idToVertexMap.get(emd.getTarget());
        if (source == null || target == null) {
            throw new GraphIOException("edge references undefined source or target vertex. " + "Source: " + emd.getSource() + ", Target: " + emd.getTarget());
        }
        // Add it to the graph. 
        if (graph instanceof Graph) {
            ((Graph<V, E>) graph).addEdge(edge, source, target, emd.isDirected() ? EdgeType.DIRECTED : EdgeType.UNDIRECTED);
        } else {
            graph.addEdge(edge, new Pair<V>(source, target));
        }
    }
}
