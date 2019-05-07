void method0() { 
/**
	 * True if the cells should be auto-sized when their values change. Default
	 * is false.
	 */
protected boolean autoSizeOnValueChange = false;
/**
	 * Boolean indicating whether existing connections should me made visible if
	 * their sources or targets are made visible, given the opposite end of the
	 * edge is already visible or made visible, too. Default is true.
	 */
protected boolean showsExistingConnections = true;
/**
	 * Boolean indicating whether connections should be made visible when
	 * reconnected and their source and target port is visible. Default is true.
	 */
protected boolean showsChangedConnections = true;
/**
	 * Boolean indicating whether edited cells should be made visible if they
	 * are changed via
	 * {@link #edit(Map, ConnectionSet, ParentMap, UndoableEdit[])}. Default is
	 * false.
	 */
protected boolean showsInvisibleEditedCells = false;
/**
	 * Boolean indicating whether inserted should be made visible if they are
	 * inserted via
	 * {@link #insert(Object[], Map, ConnectionSet, ParentMap, UndoableEdit[])}.
	 * Default is true.
	 */
protected boolean showsInsertedCells = true;
/**
	 * Boolean indicating whether inserted edges should me made visible if their
	 * sources or targets are already visible. Default is true.
	 */
protected boolean showsInsertedConnections = true;
/**
	 * Boolean indicating whether existing connections should be hidden if their
	 * source or target and no parent of the ports is visible, either by hiding
	 * the cell or by changing the source or target of the edge to a hidden
	 * cell. Default is true.
	 */
protected boolean hidesExistingConnections = true;
/**
	 * Boolean indicating whether existing connections should be hidden if their
	 * source or target port is removed from the model. Default is false.
	 */
protected boolean hidesDanglingConnections = false;
/**
	 * Boolean indicating whether cellviews should be remembered once visible in
	 * this GraphLayoutCache. Default is true.
	 */
protected boolean remembersCellViews = true;
/**
	 * Boolean indicating whether inserted cells should automatically be
	 * selected. Default is true. This is ignored if the cache is partial. Note:
	 * Despite the name of this field the implementation is located in the
	 * BasicGraphUI.GraphModelHandler.graphChanged method.
	 */
protected boolean selectsAllInsertedCells = false;
/**
	 * Boolean indicating whether cells that are inserted using the local insert
	 * method should automatically be selected. Default is true. This is ignored
	 * if the cache is not partial and selectsAllInsertedCells is true, in which
	 * case the cells will be selected through another mechanism. Note: Despite
	 * the name of this field the implementation is located in the
	 * BasicGraphUI.GraphLayoutCacheObserver.changed method.
	 */
protected boolean selectsLocalInsertedCells = false;
/**
	 * Boolean indicating whether children should be moved to the parent group's
	 * origin on expand. Default is true.
	 */
protected boolean movesChildrenOnExpand = true;
/**
	 * Boolean indicating whether parents should be moved to the child area
	 * origin on collapse. Default is true.
	 */
protected boolean movesParentsOnCollapse = true;
/**
	 * Boolean indicating whether parents should always be resized to the child
	 * area on collapse. If false the size is only initially updated if it has
	 * not yet been assigned. Default is false.
	 */
protected boolean resizesParentsOnCollapse = false;
/**
	 * Specified the initial x- and y-scaling factor for initial collapsed group
	 * bounds. Default is 1.0, ie. no scaling.
	 */
protected double collapseXScale = 1.0, collapseYScale = 1.0;
/**
	 * Boolean indicating whether edges should be reconneted to visible parents
	 * on collapse/expand. Default is false.
	 * 
	 * @deprecated edges are moved to parent view and back automatically
	 */
protected boolean reconnectsEdgesToVisibleParent = false;
/**
	 * The list of listeners that listen to the GraphLayoutCache.
	 */
protected EventListenerList listenerList = new EventListenerList();
/**
	 * Reference to the graphModel
	 */
protected GraphModel graphModel;
/**
	 * Maps cells to views.
	 */
protected Map mapping = new Hashtable();
/**
	 * Maps cells to views. The hidden mapping is used to remembed cell views
	 * that are hidden, based on the remembersCellViews setting. hiddenMapping
	 * must use weak keys for the cells since when cells are removed
	 * hiddenMapping is not updated.
	 */
protected transient Map hiddenMapping = new WeakHashMap();
/**
	 * Factory to create the views.
	 */
protected CellViewFactory factory = null;
/**
	 * The set of visible cells.
	 */
protected Set visibleSet = new HashSet();
/**
	 * Ordered list of roots for the view.
	 */
protected List roots = new ArrayList();
/**
	 * Cached array of all ports for the view.
	 */
protected PortView[] ports;
/**
	 * Only portions of the model are visible.
	 */
protected boolean partial = false;
/**
	 * Controls if all attributes are local. If this is false then the
	 * createLocalEdit will check the localAttributes set to see if a specific
	 * attribute is local, otherwise it will assume that all attributes are
	 * local. This allows to make all attributes local without actually knowing
	 * them. Default is false.
	 */
protected boolean allAttributesLocal = false;
/**
	 * A set containing all attribute keys that are stored in the cell views, in
	 * other words, the view-local attributes.
	 */
protected Set localAttributes = new HashSet();
}
