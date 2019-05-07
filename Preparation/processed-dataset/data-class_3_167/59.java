/**
	 * Inserts the specified edge into the graph model. This method does in fact
	 * nothing, it calls insert with a default connection set.
	 * 
	 * @param edge
	 *            the edge to be inserted
	 * @param source
	 *            the source port this edge is connected to
	 * @param target
	 *            the target port this edge is connected to
	 */
public void insertEdge(Object edge, Object source, Object target) {
    insert(new Object[] { edge }, new Hashtable(), new ConnectionSet(edge, source, target), new ParentMap());
}
