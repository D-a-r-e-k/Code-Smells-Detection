/**
	 * Inserts the specified vertex into the graph model. This method does in
	 * fact nothing, it calls insert edge with the vertex and the source and
	 * target port set to null. This example shows how to add a vertex with a
	 * port and a black border:
	 * 
	 * <pre>
	 * DefaultGraphCell vertex = new DefaultGraphCell(&quot;Hello, world!&quot;);
	 * Map attrs = vertex.getAttributes();
	 * GraphConstants.setOpaque(attrs, false);
	 * GraphConstants.setBorderColor(attrs, Color.black);
	 * DefaultPort port = new DefaultPort();
	 * vertex.add(port);
	 * port.setParent(vertex);
	 * graph.getGraphLayoutCache().insert(vertex);
	 * </pre>
	 * 
	 * @param cell
	 *            inserts the specified cell in the cache
	 */
public void insert(Object cell) {
    insert(new Object[] { cell });
}
