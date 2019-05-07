/******************************************************************************/
/**
 * Transforms the Edges stored in a given List of edges into an array of lines.
 * This is usefull, to get the Positions of the Edges.
 * @param edges List containing only EdgeViews
 * @return Array of Lines representing the edges of the graph.
 */
private Line2D.Double[] getEdgeLines(ArrayList edges) {
    Line2D.Double[] lines = new Line2D.Double[edges.size()];
    for (int i = 0; i < edges.size(); i++) {
        EdgeView edge = (EdgeView) edges.get(i);
        CellView source = edge.getSource().getParentView();
        CellView target = edge.getTarget().getParentView();
        lines[i] = new Line2D.Double(getPosition(source), getPosition(target));
    }
    return lines;
}
