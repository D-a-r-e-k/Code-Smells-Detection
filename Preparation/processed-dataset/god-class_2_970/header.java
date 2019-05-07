void method0() { 
/** Const to add Attributes at the Nodes
	 *
	 */
public static final String SUGIYAMA_VISITED = "SugiyamaVisited";
/** Const to add the Cell Wrapper to the Nodes
	 */
public static final String SUGIYAMA_CELL_WRAPPER = "SugiyamaCellWrapper";
/** 
	 * Const to add Attributes at the Nodes indicating that the cell was explicitly specified to the layout.
     *
     * @see #run(org.jgraph.JGraph,Object[],Object[])
	 */
public static final String SUGIYAMA_SELECTED = "SugiyamaSelected";
/** represents the size of the grid in horizontal grid elements
	 *
	 */
protected int gridAreaSize = Integer.MIN_VALUE;
/** A list with Integer Objects. The list contains the
	 *  history of movements per loop
	 *  It was needed for the progress dialog
	 */
List movements = null;
/** Represents the movements in the current loop.
	 *  It was needed for the progress dialog
	 */
int movementsCurrentLoop = -1;
/** Represents the maximum of movements in the current loop.
	 *  It was needed for the progress dialog
	 */
int movementsMax = Integer.MIN_VALUE;
/** Represents the current loop number
	 *  It was needed for the progress dialog
	 */
int iteration = 0;
/**
     * The default layout direction is vertical (top-down)
     */
protected boolean vertical = true;
/**
     * The default grid spacing is (250, 150).
     */
protected Point spacing = new Point(250, 150);
/**
     * Controls whether the graph should be placed as close to the origin as possible.
     */
protected boolean flushToOrigin = false;
}
