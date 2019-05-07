/******************************************************************************/
/**
 * Returns a list of all cells, that have a direct connection with the given
 * cell via a edge. At the end of this method, the result is stored in the given
 * cell, so it will be available the next time, the method runs. This temporary
 * stored data will stay there, until the algorithm finishes 
 * (successfull or not). 
 * 
 * @param view Cell, the relatives requested from.
 * @return List of all cells, that have a direct connection with a edge to
 * the given cell. 
 */
private ArrayList getRelatives(CellView view) {
    if (!(view instanceof VertexView)) {
        new Exception("getRelatives 1").printStackTrace();
        return null;
    }
    if (view.getAttributes().containsKey(KEY_RELATIVES))
        return (ArrayList) view.getAttributes().get(KEY_RELATIVES);
    ArrayList relatives = new ArrayList();
    //if view is a cluster, then all clustered cells are extracted and 
    //getRelatives is called for every cell again. the resulting relatives 
    //are checked, if they are in the same cluster or another cluster. 
    //if the last condition is the case, the cluster is added, else the 
    //vertex is added to the list of relatives, iff he isn't already in the 
    //list 
    if (isCluster(view)) {
        ArrayList clusteredVertices = (ArrayList) view.getAttributes().get(KEY_CLUSTERED_VERTICES);
        for (int i = 0; i < clusteredVertices.size(); i++) {
            ArrayList vertexRelatives = getRelatives((CellView) clusteredVertices.get(i));
            for (int j = 0; j < vertexRelatives.size(); j++) {
                CellView relative = (CellView) vertexRelatives.get(j);
                if (!clusteredVertices.contains(relative)) {
                    /*                      if( relative.getAttributes().containsKey(KEY_CLUSTER) ){
                relative = (CellView) relative.getAttributes().get(KEY_CLUSTER);
                        }*/
                    if (!relatives.contains(relative))
                        relatives.add(relative);
                }
            }
        }
    } else {
        //runs only for vertices. finds all ports of the vertex. every 
        //edge in every port is checked on their source and target. 
        //the one, that isn't the vertex, we are searching the relatives of, 
        //is added to the list of relatives. 
        ArrayList portsCells = new ArrayList();
        VertexView vertexView = (VertexView) view;
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
                CellView nextVertex = mapper.getMapping(model.getParent(nextPort), true);
                relatives.add(nextVertex);
            }
        }
    }
    view.getAttributes().put(KEY_RELATIVES, relatives);
    return relatives;
}
