protected void displayHelper(VertexView view) {
    TreeLayoutNode node = getTreeLayoutNode(view);
    /*
        Object cell = view.getCell();
        Map attributes =
                GraphConstants.createAttributes(
                        cell,
                        GraphConstants.BOUNDS,
                        new Rectangle(node.pos, new Dimension(node.width, node.height)));

        jgraph.getGraphLayoutCache().edit(attributes, null, null, null);
        */
    Rectangle2D rect = view.getBounds();
    rect.setFrame(node.pos.x, node.pos.y, node.width, node.height);
    List c = getChildren(view);
    for (Iterator iterator = c.iterator(); iterator.hasNext(); ) {
        VertexView vertexView = (VertexView) iterator.next();
        displayHelper(vertexView);
    }
    view.getAttributes().remove(CELL_WRAPPER);
}
