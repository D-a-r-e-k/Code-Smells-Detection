/******************************************************************************/
/**
 * Makes the changed Positions of the Cells of the graph visible. This is like
 * a "commit". Before this Method runs, nothing has change is the visible 
 * representation of the graph. After this method, the Layout for the Cells
 * of the graph is applied.
 */
private void applyChanges() {
    Map viewMap = new Hashtable();
    for (int i = 0; i < applyCellList.size(); i++) {
        CellView view = (CellView) applyCellList.get(i);
        Point2D.Double pos = getPosition(view);
        Rectangle2D r = view.getBounds();
        r.setFrame(pos.getX() - (r.getWidth() / 2.0), pos.getY() - (r.getHeight() / 2.0), r.getWidth(), r.getHeight());
        Object cell = ((CellView) applyCellList.get(i)).getCell();
        Map attributes = new Hashtable();
        GraphConstants.setBounds(attributes, r);
        viewMap.put(cell, attributes);
    }
    jgraph.getGraphLayoutCache().edit(viewMap, null, null, null);
}
