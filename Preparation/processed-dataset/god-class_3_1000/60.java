/**
	 * Inserts the specified cells into the graph model. This method is a
	 * general implementation of cell insertion. If the source and target port
	 * are null, then no connection set is created. The method uses the
	 * attributes from the specified edge and the egdge's children to construct
	 * the insert call. This example shows how to insert an edge with a special
	 * arrow between two known vertices:
	 * 
	 * <pre>
	 * Object source = graph.getDefaultPortForCell(sourceVertex).getCell();
	 * Object target = graph.getDefaultPortForCell(targetVertex).getCell();
	 * DefaultEdge edge = new DefaultEdge(&quot;Hello, world!&quot;);
	 * edge.setSource(source);
	 * edge.setTarget(target);
	 * Map attrs = edge.getAttributes();
	 * GraphConstants.setLineEnd(attrs, GraphConstants.ARROW_TECHNICAL);
	 * graph.getGraphLayoutCache().insert(edge);
	 * </pre>
	 */
public void insert(Object[] cells) {
    insert(cells, new Hashtable(), new ConnectionSet(), new ParentMap());
}
