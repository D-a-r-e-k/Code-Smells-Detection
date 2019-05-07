void method0() { 
/**
	 * Reference to the layout dialog which is used to configure the current
	 * layout.
	 */
protected transient JDialog configDialog = null;
/**
	 * Properties panel used inside the layout configuration dialog.
	 */
protected transient PropertySheetPanel propertySheet = new PropertySheetPanel();
/**
	 * Reference to the hosting graph which is in charge of animating changes to
	 * the geometry (morphing).
	 */
protected transient JGraphExampleGraph graph;
/**
	 * Switches to create the facade and to fetch the result from the layout.
	 */
protected boolean ignoreChildren = true, ignoreHidden = true, ignoreUnconnected = true, layoutDirectedGraph = true, layoutIgnoreGrid = true, layoutFlushOrigin = true, layoutMoveSelection = true, morphing = true;
/**
	 * Specifies if all manual changes should trigger an autolayout.
	 */
protected transient boolean autolayout = false;
/**
	 * Specifies the current layout algorithm.
	 */
protected transient JGraphLayout layout;
}
