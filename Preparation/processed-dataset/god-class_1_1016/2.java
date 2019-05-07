private void addVerticesToGraph(G graph, Collection<V> vertices) {
    for (V vertex : vertices) {
        graph.addVertex(vertex);
    }
}
