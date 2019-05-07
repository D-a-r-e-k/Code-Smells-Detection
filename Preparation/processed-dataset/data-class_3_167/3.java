/**
	 * Invoke this method after you've changed how the cells are to be
	 * represented in the graph.
	 */
public void cellViewsChanged(final CellView[] cellViews) {
    if (cellViews != null) {
        fireGraphLayoutCacheChanged(this, new GraphLayoutCacheEvent.GraphLayoutCacheChange() {

            public Object[] getInserted() {
                return null;
            }

            public Object[] getRemoved() {
                return null;
            }

            public Map getPreviousAttributes() {
                return null;
            }

            public Object getSource() {
                return this;
            }

            public Object[] getChanged() {
                return cellViews;
            }

            public Map getAttributes() {
                return null;
            }

            public Object[] getContext() {
                return null;
            }

            public Rectangle2D getDirtyRegion() {
                return null;
            }

            public void setDirtyRegion(Rectangle2D dirty) {
            }
        });
    }
}
