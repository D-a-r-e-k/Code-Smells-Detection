protected void display(Collection roots) {
    for (Iterator iterator = roots.iterator(); iterator.hasNext(); ) {
        VertexView vertexView = (VertexView) iterator.next();
        displayHelper(vertexView);
    }
}
