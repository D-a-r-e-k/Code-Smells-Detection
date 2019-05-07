/******************************************************************************/
/**
 * Costfunction. This method calculates the distance between Cells and Edges.
 * A small distance brings up higher costs while great distances generates lower
 * costs. Costs for the distance between Cells and Edges are always computed
 * by the method. If the distance is smaller than {@link #minDistance} 
 * additional costs are added. This method is suggested for finetuning and other
 * short running calculations. Its the slowest of all costfunctions implemented 
 * here.
 * 
 * @param lambda A normalizing factor for this function. Drawings with a
 * relativ increase lambda will have greater distances between nodes and
 * edges, by the expense of other aesthetics.
 */
private double getEdgeDistance(double lambda) {
    double energy = 0.0;
    for (int i = 0; i < applyCellList.size(); i++) {
        double h = 0.0;
        CellView view = (CellView) applyCellList.get(i);
        ArrayList relevantEdges = null;
        if (view.getAttributes().containsKey(CF_KEY_EDGE_DISTANCE_RELEVANT_EDGES)) {
            relevantEdges = (ArrayList) view.getAttributes().get(CF_KEY_EDGE_DISTANCE_RELEVANT_EDGES);
        } else {
            relevantEdges = getRelevantEdges(view);
            view.getAttributes().put(CF_KEY_EDGE_DISTANCE_RELEVANT_EDGES, relevantEdges);
        }
        Line2D.Double[] lineList = getEdgeLines(getRelevantEdges(view));
        for (int j = 0; j < lineList.length; j++) {
            double distance = lineList[j].ptSegDist(getPosition(view));
            //prevents from dividing with Zero 
            if (Math.abs(distance) < equalsNull)
                distance = equalsNull;
            if (distance != 0.0)
                h += lambda / (distance * distance);
            if (distance < minDistance)
                h += lambda / (minDistance * minDistance);
        }
        energy += h;
    }
    //        System.out.println("EdgeDistance     : "+energy); 
    return energy;
}
