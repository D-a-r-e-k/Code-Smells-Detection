/******************************************************************************/
/**
 * Workaround for a BUG. When 
 * {@link #graphChanged(GraphModelEvent) graphChanged(...)} is called, the 
 * method gets via myGraphModelEvent.getChanged().getInserted() an array of
 * objects. This array consists of the key's to the views inserted into the 
 * graph. When this views are gained, the BUG appears. The array gained from
 * the GraphLayoutCache contains only VertexView's. Instead of the EdgeViews 
 * there is NULL in the array. This method is callen if this BUG appears, in the
 * hope, to get the inserted edges.  
 */
private void getAllEdges() {
    Object[] cells = jgraph.getRoots();
    CellView[] views = jgraph.getGraphLayoutCache().getMapping(cells, false);
    for (int i = 0; i < views.length; i++) {
        if (views[i] instanceof VertexView) {
            VertexView vertexView = (VertexView) views[i];
            GraphModel model = jgraph.getModel();
            CellMapper mapper = jgraph.getGraphLayoutCache();
            Object vertexCell = vertexView.getCell();
            ArrayList portsCells = new ArrayList();
            for (int j = 0; j < model.getChildCount(vertexCell); j++) {
                Object portCell = model.getChild(vertexCell, j);
                portsCells.add(portCell);
            }
            for (int j = 0; j < portsCells.size(); j++) {
                Object portCell = portsCells.get(j);
                Iterator edges = model.edges(portCell);
                while (edges.hasNext()) {
                    Object edge = edges.next();
                    Object e = mapper.getMapping(edge, false);
                    if (!edgeList.contains(e) && e != null) {
                        edgeList.add(e);
                    }
                }
            }
        } else if (views[i] instanceof EdgeView) {
            if (!edgeList.contains(views[i]) && views[i] != null) {
                edgeList.add(views[i]);
            }
        }
    }
}
