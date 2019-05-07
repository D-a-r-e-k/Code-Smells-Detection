protected List getChildren(VertexView node) {
    ArrayList children = new ArrayList();
    Object vertex = node.getCell();
    GraphModel model = jgraph.getModel();
    int portCount = model.getChildCount(vertex);
    // iterate any NodePort 
    for (int i = 0; i < portCount; i++) {
        Object port = model.getChild(vertex, i);
        // iterate any Edge in the port 
        Iterator itrEdges = model.edges(port);
        while (itrEdges.hasNext()) {
            Object edge = itrEdges.next();
            // if the Edge is a forward edge we should follow this edge 
            if (port == model.getSource(edge)) {
                Object targetPort = model.getTarget(edge);
                Object targetVertex = model.getParent(targetPort);
                VertexView targetVertexView = (VertexView) jgraph.getGraphLayoutCache().getMapping(targetVertex, false);
                children.add(targetVertexView);
            }
        }
    }
    return children;
}
