@SuppressWarnings("unchecked")
private void addHyperEdgesToGraph(G graph, Collection<HyperEdgeMetadata> metadata, Map<String, V> idToVertexMap) throws GraphIOException {
    for (HyperEdgeMetadata emd : metadata) {
        // Get the edge out of the metadata 
        E edge = (E) emd.getEdge();
        // Add the verticies to a list. 
        List<V> verticies = new ArrayList<V>();
        List<EndpointMetadata> endpoints = emd.getEndpoints();
        for (EndpointMetadata ep : endpoints) {
            V v = idToVertexMap.get(ep.getNode());
            if (v == null) {
                throw new GraphIOException("hyperedge references undefined vertex: " + ep.getNode());
            }
            verticies.add(v);
        }
        // Add it to the graph. 
        graph.addEdge(edge, verticies);
    }
}
