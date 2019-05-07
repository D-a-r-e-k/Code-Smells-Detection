/******************************************************************************/
/**
 * Costfunction. This criterion tries to shorten the edges to a necessary 
 * minimum without causing the entire graph to become to tightly packed.
 * This function penalizes long edges.
 * 
 * @param lambda An appropriate normalizing factor. Increasing lamda relative
 * to the lambdas of other costfunctions will result in shorter Edges. 
 * Decreasing brings up very different length of the edges.
 * @return costs of this criterion
 */
private double getEdgeLength(double lambda) {
    double energy = 0.0;
    Line2D.Double[] lineList = getEdgeLines(edgeList);
    for (int i = 0; i < lineList.length; i++) {
        Point2D p1 = lineList[i].getP1();
        Point2D p2 = lineList[i].getP2();
        double edgeLength = p1.distance(p2);
        energy += lambda * edgeLength * edgeLength;
    }
    //        System.out.println("EdgeLength       : "+energy);         
    return energy;
}
