void method0() { 
/**
		 * The actual graph cell this wrapper represents
		 */
protected Object cell;
/**
		 * All edge that repel this cell, only used for nodes. This array
		 * is equivalent to all edges unconnected to this node
		 */
protected int[] relevantEdges = null;
/**
		 * the index of all connected edges in the <code>e</code> array
		 * to this node. This is only used for nodes.
		 */
protected int[] connectedEdges = null;
/**
		 * The x-coordinate position of this cell, nodes only
		 */
protected double x;
/**
		 * The y-coordinate position of this cell, nodes only
		 */
protected double y;
/**
		 * The approximate radius squared of this cell, nodes only. If
		 * approxNodeDimensions is true on the layout this value holds the
		 * width of the node squared
		 */
protected double radiusSquared;
/**
		 * The height of the node squared, only used if approxNodeDimensions
		 * is set to true.
		 */
protected double heightSquared;
/**
		 * The index of the node attached to this edge as source, edges only
		 */
protected int source;
/**
		 * The index of the node attached to this edge as target, edges only
		 */
protected int target;
}
