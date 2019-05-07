void method0() { 
/**
	 * The minimum buffer between cells on the same rank
	 */
protected double intraCellSpacing = 30.0;
/**
	 * The minimum distance between cells on adjacent ranks
	 */
protected double interRankCellSpacing = 30.0;
/**
	 * The distance between each parallel edge on each ranks for long edges
	 */
protected double parallelEdgeSpacing = 10.0;
/**
	 * The number of heuristic iterations to run
	 */
protected int maxIterations = 8;
/**
	 * The position of the root ( start ) node(s) relative to the rest of the
	 * laid out graph
	 */
protected int orientation = SwingConstants.NORTH;
/**
	 * The minimum x position node placement starts at
	 */
protected double initialX;
/**
	 * The maximum x value this positioning lays up to
	 */
protected double limitX;
/**
	 * The sum of x-displacements for the current iteration
	 */
protected double currentXDelta;
/**
	 * The rank that has the widest x position
	 */
protected int widestRank;
/**
	 * The X-coordinate of the edge of the widest rank
	 */
protected double widestRankValue;
/**
	 * The width of all the ranks
	 */
protected double[] rankWidths;
/*
	 * The y co-ordinate of all the ranks
	 */
protected double[] rankY;
/**
	 * Whether or not to perform local optimisations and iterate multiple times
	 * through the algorithm
	 */
protected boolean fineTuning = true;
/**
	 * Whether or not to pull together sections of layout into empty space
	 */
protected boolean compactLayout = false;
/**
	 * A store of connections to the layer above for speed
	 */
protected JGraphAbstractHierarchyCell[][] nextLayerConnectedCache;
/**
	 * A store of connections to the layer below for speed
	 */
protected JGraphAbstractHierarchyCell[][] previousLayerConnectedCache;
/** The logger for this class */
private static Logger logger = Logger.getLogger("com.jgraph.layout.hierarchical.JGraphCoordinateAssignment");
}
