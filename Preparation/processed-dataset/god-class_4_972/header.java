void method0() { 
/**
     * Key used on every cell. This key indicates that in the cell are 
     * attributes stored by this algorithm. The algorithm itself never asks for
     * this key. This is for others developers only, using this algorithm, to
     * find out, where sometimes approaching attributes come from.
     */
public static final String KEY_CAPTION = "GEM-TEMPORARY-DATA";
/**
     * Key used on every cell. Under this key every cell stores temporary the
     * temperature of itself. Temperature is a indicator how far a cell can move
     * and all temperatures together indicating how long the algorithm will run.
     */
public static final String KEY_TEMPERATURE = "Temperature";
/**
     * Key used on every cell. Under this key every cell stores temporary the
     * current force impulse affecting the cell.
     */
public static final String KEY_CURRENT_IMPULSE = "Current_Impulse";
/**
     * Key used on every cell. Under this key every cell stores temporary the
     * last impulse. This is the value of the previous 
     * {@link #KEY_CURRENT_IMPULSE}.
     */
public static final String KEY_LAST_IMPULSE = "Last_Impulse";
/**
     * Key used on every cell. Under this key every cell stores the temporary  
     * position on the display, while the calculation is running. This makes
     * the algorithm faster, than asking the Cell everytime for its position.
     * So the algorithm can anytime be canceled, whithout changing something. 
     */
public static final String KEY_POSITION = "Position";
/**
     * Key used on every cell. Under this key every cell stores the temporary
     * skew gauge. This value is for punish rotations of the cells.
     */
public static final String KEY_SKEWGAUGE = "Skew_Gauge";
/**
     * Key used on every Cell. Under this key every cell stores the cells,
     * that are only one edge away from it. The relatives are stored, after the
     * first time, they are desired and calculated by 
     * {@link #getRelatives(CellView)}.
     */
public static final String KEY_RELATIVES = "Relatives";
/**
     * Key used on every Cell. This indicates a weight for the number of edges
     * received by {@link #getNodeWeight(CellView)}
     */
public static final String KEY_MASSINDEX = "Mass_Index";
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
     * List of all nodes in the graph.
     */
private ArrayList cellList;
/** 
     * List of all nodes the algorithm should be done for. 
     */
private ArrayList applyCellList;
/** 
     * List of all edges in the graph. This is only needed for the optimization
     * Algorithm.
     */
private ArrayList edgeList;
/** 
     * needed for comperation with other double values if they are 0.0.
     */
private double equalsNull = 0.00000000000000001;
/** 
     * starting value for the temperatures of the cells 
     */
protected double initTemperature = 10;
/** 
     * if the temperature of all cells or the average of the temperatures of 
     * all cells is below this value, the algorithm stops
     */
protected double minTemperature = 3;
/** 
     * temperature will never be over this value 
     */
protected double maxTemperature = 256;
/** 
     * the length of the Edges in Pixel, the algorithm tries to keep
     */
protected double prefEdgeLength = 100;
/** 
     * the strength of the gravitation force, directed to the barycenter of the
     * graph, added to all cells. 
     */
protected double gravitation = 0.0625;
/** 
     * length of a force vector with a random direction, added to all cells. 
     */
protected double randomImpulseRange = 32;
/** 
     * opening angle in radiant, that detects oscillations 
     */
protected double alphaOsc = Math.toRadians(90);
/** 
     * opening angle in radiant, that detects rotations 
     */
protected double alphaRot = Math.toRadians(60);
/** 
     * penalty value for a detected oscillation
     */
protected double sigmaOsc = 1.0 / 3.0;
/** 
     * penalty value for a detected rotation 
     */
protected double sigmaRot = 1.0 / 2.0;
/**
     * number of rounds until the algorithm will break. This value is 
     * precalculated to a aproximativ value of 4 times the count of Cells in
     * {@link #applyCellList}
     */
private int maxRounds;
/**
     * counts the rounds
     */
private int countRounds;
/**
     * If the pathlength between an inserted cell and an allready layouted cell
     * is below this value, the allready layouted cell will be layouted again.
     */
private int recursionDepth;
/**
     * Describes the distance around a cell, that will be whatched for other
     * cells, intersecting this area. If another cell intersects, a force will
     * be added to the cell, that pushes it away.
     */
private double overlapDetectWidth;
/**
     * Describes a distance the algorithm tries to keep, when he detects a 
     * overlapping cell. 
     */
private double overlapPrefDistance;
/**
     * switches the feature to whatch for overlapping on/off
     */
private boolean avoidOverlapping;
/**
     * describes, what method will be taken, when cells are inserted. Posible
     * values are 
     */
private String layoutUpdateMethod;
/**
     * condition for method isFrozen(). decides whether the method returns true
     * when the average of all temperatures or all temperatures are below 
     * {@link #minTemperature}. 
     */
private boolean shouldEndPerAverage;
/**
     * condition for the method calculate(). decides whether the algorithm
     * is computed for the cellViews every time in the same sequence
     * or if the cellViews are computed every time in a random sequence.
     */
private boolean shouldComputePermutation;
/**
     * Switches the skill of the algorithm to perform the layout update process
     */
private boolean isActive = true;
/**
     * Checks if the algorithm is currently running. If this is the case, no
     * GraphModelEvent will be computed and no new run can be initiated.
     */
private boolean isRunning = false;
/**
     * a reference to the instance of jgraph
     */
private JGraph jgraph;
/**
     * the gpConfiguration of this algorithm
     */
protected Properties config;
/**
     * to identify for the method {@link #loadRuntimeValues(int)}, that the 
     * algorithm wants to perform a new run
     */
protected static final int VALUES_PUR = 0;
/**
     * to identify for the method {@link #loadRuntimeValues(int)}, that the 
     * algorithm wants to perform a layout update
     */
protected static final int VALUES_INC = 1;
/**
     * algorithm used for optimizing the result of this algorithm
     */
private AnnealingLayoutAlgorithm optimizationAlgorithm;
/**
     * switches the usage of the optimizing algorithm
     */
private boolean useOptimizeAlgorithm;
/**
     * gpConfiguration of the optimizing algorithm
     */
private Properties optimizationAlgorithmConfig;
/**
     * Switches clustering for the layout update process on/off
     */
private boolean isClusteringEnabled;
/**
     * The initial temperature for clusters. It is recommended, that this value
     * is lower than {@link #initTemperature} to get a good looking layout
     */
private double clusterInitTemperature;
/**
     * Scales forces, that are effecting clusters. It is recommendet to take
     * a value between 1.0 and 0.0. This garanties, that clusters move slower
     * than other cells. That rises the chance of getting a good looking layout
     * after the calculation.
     */
private double clusterForceScalingFactor;
/**
     * Effects, how many clusters are created, when the layout update process
     * starts. This affects the initial number of clusters, which is the number
     * of cells available minus the number of cells to layout. The result of
     * that term is divided by this factor, to get the maximum number of 
     * clusters. After this calculation, the clustering algorithm tries to
     * minimize the number of clusters, so there might be less clusters than 
     * the maximum number.
     */
private double clusteringFactor;
/**
     * The initial size for the layout update method perimeter. This describes
     * a radius around an inserted cell. Every other inserted cell in this
     * radius increases the radius by {@link #perimeterSizeInc}. After finishing
     * increasing the radius, every cell, from the cells, that are already 
     * layouted, in the radius is added to the list of cells, that'll gain a
     * new position during the layout update process. This should bring up
     * the behaviour, that the previous layouted cells make space for the layout 
     * of the inserted cells.
     */
private double perimeterInitSize;
/**
     * Inserted cells whithin a radius of {@link #perimeterInitSize} around
     * a inserted cell are counted. After counting the inserted cells around
     * a inserted cell, the initial radius is increased by this increase value
     * times the number of inserted cells around the inserted cell. Every
     * previous layouted cell in the resulting radius around the inserted cell
     * is going to be layouted again.
     */
private double perimeterSizeInc;
}
