/******************************************************************************/
/**
 * Retrieves all Cells that have an edge with the given Cell.
 * @param view Cell, the relatives are requested from
 * @return Relatives of view
 */
protected ArrayList getRelatives(CellView view) {
    if (view.getAttributes().containsKey(KEY_RELATIVES))
        return (ArrayList) view.getAttributes().get(KEY_RELATIVES);
    ArrayList relatives = new ArrayList();
    ArrayList portsCells = new ArrayList();
    VertexView vertexView = (VertexView) view;
    if (isCluster(view)) {
        ArrayList clusteredVertices = (ArrayList) vertexView.getAttributes().get(KEY_CLUSTERED_VERTICES);
        for (int i = 0; i < clusteredVertices.size(); i++) {
            ArrayList clusterRelatives = getRelatives((CellView) clusteredVertices.get(i));
            for (int j = 0; j < clusterRelatives.size(); j++) if (!relatives.contains(clusterRelatives.get(j)) && !clusteredVertices.contains(clusterRelatives.get(j))) {
                relatives.add(clusterRelatives.get(j));
            }
        }
    } else {
        GraphModel model = jgraph.getModel();
        CellMapper mapper = jgraph.getGraphLayoutCache();
        Object vertexCell = vertexView.getCell();
        for (int i = 0; i < model.getChildCount(vertexCell); i++) {
            Object portCell = model.getChild(vertexCell, i);
            portsCells.add(portCell);
        }
        for (int i = 0; i < portsCells.size(); i++) {
            Object portCell = portsCells.get(i);
            Iterator edges = model.edges(portCell);
            while (edges.hasNext()) {
                Object edge = edges.next();
                Object nextPort = null;
                if (model.getSource(edge) != portCell) {
                    nextPort = model.getSource(edge);
                } else {
                    nextPort = model.getTarget(edge);
                }
                CellView nextVertex = mapper.getMapping(model.getParent(nextPort), false);
                relatives.add(nextVertex);
            }
        }
    }
    view.getAttributes().put(KEY_RELATIVES, relatives);
    return relatives;
}
