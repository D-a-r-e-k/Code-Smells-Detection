void method0() { 
public static final int COUT_COSTFUNCTION = 6;
public static final int COSTFUNCTION_EDGE_DISTANCE = 1;
public static final int COSTFUNCTION_EDGE_CROSSING = 2;
public static final int COSTFUNCTION_EDGE_LENGTH = 4;
public static final int COSTFUNCTION_BORDERLINE = 8;
public static final int COSTFUNCTION_NODE_DISTRIBUTION = 16;
public static final int COSTFUNCTION_NODE_DISTANCE = 32;
public static final String KEY_CAPTION = "Annealing Layoutalgorithm Attributes";
public static final String KEY_POSITION = "Position";
public static final String KEY_RELATIVES = "Relatives";
public static final String CF_KEY_EDGE_DISTANCE_RELEVANT_EDGES = "costfunction edge distance key for relevant edges";
/**
     * Key used only with clusters. Under this key a cluster has an ArrayList.
     * This list is filled with the clustered vertices.
     * @see #clusterGraph()
     */
public static final String KEY_CLUSTERED_VERTICES = "Clustered Vertices";
/**
     * Key used only with clusters. Under this key vertices have the cluster
     * they belong to.
     * @see #clusterGraph()
     */
public static final String KEY_CLUSTER = "Cluster";
/**
     * Key used only with clusters. Under this key a cluster has a boolean value
     * indicating that this vertice is a cluster (clusters are 
     * VertexView-instances like every other cell).
     * @see #clusterGraph() 
     */
public static final String KEY_IS_CLUSTER = "is Cluster";
/**
     * Key used only with clusters. Under this key every cluster has a position,
     * which represents the position of the cluster, right after the clustering
     * process. After the layout update process is finished, the move, resulting
     * of subtracting the position under {@link #KEY_POSITION} from the 
     * position under this value, will be performed to all vertices in the 
     * cluster. By holding the initial position here clustering becomes 
     * possible.
     * 
     * @see #clusterGraph()
     * @see #declusterGraph()
     */
public static final String KEY_CLUSTER_INIT_POSITION = "initial Position of the Cluster";
/**
     * Key for loading gpConfiguration values. Indicates to load values for a
     * normal run.
     */
protected static final int CONFIG_KEY_RUN = 0;
/**
     * Key for loading gpConfiguration values. Indicates to load values for a
     * layout update.
     */
protected static final int CONFIG_KEY_LAYOUT_UPDATE = 1;
/**
     * actual temperature
     */
private double temperature;
/**
     * starting temperature
     */
private double initTemperature = 40;
/**
     * when {@link #temperature} reaches this value, the algorithm finishes its
     * calculation.
     */
private double minTemperature = 2;
/**
     * value for costfunctions and getEdgeDistribution.
     * Determines, how long the edges have to
     * be.
     */
private double minDistance = 50;
/**
     * {@link #temperature} will be multiplied with this value every round
     */
private double tempScaleFactor = 0.95;
/**
     * maximum number of rounds, if algorithm doesn't stop earlier, by 
     * temperature decreasement.
     */
private int maxRounds = 10000;
/**
     * normalizing and priority factors for the costfunctions
     */
protected double[] lambdaList = new double[] { 1000, 100000, 0.02, 2000, 150, 1000000 };
/**
     * the drawing area, the graph should be layouted in.
     */
private Rectangle bounds = new Rectangle(0, 0, 1000, 700);
/**
     * determines, if the cells of the graph are computed every time in the
     * same order or a random order, calculated every round.
     */
private boolean computePermutation = true;
/**
     * determines, if the only allowed moves for cells of the graph are moves, 
     * that cost less. 
     */
private boolean uphillMovesAllowed = true;
/**
     * Indicates, if the algorithm should also run on Updates in the graph.
     */
private boolean isLayoutUpdateEnabled = true;
/**
     * Indicates what costfunctions to use for calculating the costs of the 
     * graph. The bits of this Integer switches the functions. Possible Values
     * are <br>
     * <blockquote><blockquote>
     * {@link AnnealingLayoutAlgorithm#COSTFUNCTION_NODE_DISTRIBUTION 
     * COSTFUNCTION_NODE_DISTRIBUTION}<br>
     * {@link AnnealingLayoutAlgorithm#COSTFUNCTION_NODE_DISTANCE
     * COSTFUNCTION_NODE_DISTANCE}<br>
     * {@link AnnealingLayoutAlgorithm#COSTFUNCTION_BORDERLINE
     * COSTFUNCTION_BORDERLINE}<br>
     * {@link AnnealingLayoutAlgorithm#COSTFUNCTION_EDGE_DISTANCE
     * COSTFUNCTION_EDGE_DISTANCE}<br>
     * {@link AnnealingLayoutAlgorithm#COSTFUNCTION_EDGE_CROSSING
     * COSTFUNCTION_EDGE_CROSSING}<br>
     * </blockquote></blockquote>
     */
private int costFunctionConfig = Integer.parseInt("111110", 2);
/**
     * counts the rounds 
     */
private int round;
/**
     * determines, in how many segments the circle around cells is divided,
     * to find a new position for the cell.
     */
private int triesPerCell = 8;
/**
     * the list of all cells of the graph
     */
protected ArrayList cellList;
/**
     * the list of all edges of the graph
     */
protected ArrayList edgeList;
/**
     * the list of all cells, a new layout should be calculated for
     */
protected ArrayList applyCellList;
/**
     * the JGraph
     */
private JGraph jgraph;
/**
     * holds the gpConfiguration of the algorithm, gained by the controller
     */
protected Properties presetConfig;
/**
     * for debugging purpose. 
     */
//    private long      time = 0; 
/**
     * for debugging purpose
     */
//    private boolean isDebugging = false; 
/**
     * indicates if the algorihm is performing a calculation. this prevents from
     * entering the method {@link #graphChanged(GraphModelEvent) 
     * graphChanged(...)} more than once at a time.
     */
private boolean isRunning = false;
/**
     * the number of edges, neighbors of inserted cells are away,
     * to be also layouted again.
     */
private int luRecursionDepth = 1;
/**
     * if a cell has a lower distance to a inserted cell, after the cell gained 
     * its initial position, it will be layouted too
     */
private double luPerimeterRadius = 100;
/**
     * if more than one cell is inserted and the initial position of other 
     * inserted cells is inside {@link #luPerimeterRadius} around a initial
     * position of a inserted cell, than {@link #luPerimeterRadius} will be
     * increased by this value.
     */
private double luPerimeterRadiusInc = 20;
/**
     * determines how the neighborhood is handled, when a layout update
     * should be performed. Possible values are:<p>
     * <blockquote><blockquote>
     * </blockquote></blockquote>
     */
private String luMethod = AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_METHOD_PERIMETER;
/**
     * prevents from dividing with zero and from creating to high costs
     */
private double equalsNull = 0.05;
/**
     * Switches clustering for the layout update process on/off
     */
private boolean isClusteringEnabled = true;
/**
     * Scales movement of clusters. It is recommendet to take
     * a value between 1.0 and 0.0. This garanties, that clusters move slower
     * than other cells. That rises the chance of getting a good looking layout
     * after the calculation.
     */
private double clusterMoveScaleFactor = 0.1;
/**
     * Effects, how many clusters are created, when the layout update process
     * starts. This affects the initial number of clusters, which is the number
     * of cells available minus the number of cells to layout. The result of
     * that term is divided by this factor, to get the maximum number of 
     * clusters. After this calculation, the clustering algorithm tries to
     * minimize the number of clusters, so there might be less clusters than 
     * the maximum number.
     */
private double clusteringFactor = 8.0;
protected boolean isOptimizer = false;
}
