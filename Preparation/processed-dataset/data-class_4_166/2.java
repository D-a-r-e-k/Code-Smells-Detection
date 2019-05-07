/**
	 * Configure and return the renderer based on the passed in components. The
	 * value is typically set from messaging the graph with
	 * <code>convertValueToString</code>.
	 * 
	 * @param graph
	 *            the graph that that defines the rendering context.
	 * @param view
	 *            the cell view that should be rendered.
	 * @param sel
	 *            whether the object is selected.
	 * @param focus
	 *            whether the object has the focus.
	 * @param preview
	 *            whether we are drawing a preview.
	 * @return the component used to render the value.
	 */
public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview) {
    if (view instanceof EdgeView && graph != null) {
        this.gridColor = graph.getGridColor();
        this.lockedHandleColor = graph.getLockedHandleColor();
        this.highlightColor = graph.getHighlightColor();
        this.isMoveBelowZero = graph.isMoveBelowZero();
        this.graph = new WeakReference(graph);
        this.focus = focus;
        this.selected = sel;
        this.preview = preview;
        this.childrenSelected = graph.getSelectionModel().isChildrenSelected(view.getCell());
        setView(view);
        return this;
    }
    return null;
}
