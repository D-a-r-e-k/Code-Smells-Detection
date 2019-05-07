/******************************************************************************/
/**
 * Calculates the costs of the actual graph by using costfunctions.
 * @param lambda Normalizing and priority values for the costfunctions
 * @return costs for the actual graph
 * @see #costFunctionConfig
 * @see #getBorderline(double)
 * @see #getEdgeDistance(double)
 * @see #getEdgeLength(double)
 * @see #getNodeDistance(double)
 * @see #getNodeDistribution(double)
 */
private double getGlobalCosts(double[] lambda) {
    //assert lambda.length != COUT_COSTFUNCTION; 
    //        long startTime = System.currentTimeMillis(); 
    double energy = 0.0;
    if ((costFunctionConfig & COSTFUNCTION_NODE_DISTANCE) != 0) {
        energy += getNodeDistance(lambda[5]);
    }
    if ((costFunctionConfig & COSTFUNCTION_NODE_DISTRIBUTION) != 0) {
        energy += getNodeDistribution(lambda[0]);
    }
    if ((costFunctionConfig & COSTFUNCTION_BORDERLINE) != 0) {
        energy += getBorderline(lambda[1]);
    }
    if ((costFunctionConfig & COSTFUNCTION_EDGE_LENGTH) != 0) {
        energy += getEdgeLength(lambda[2]);
    }
    if ((costFunctionConfig & COSTFUNCTION_EDGE_CROSSING) != 0) {
        energy += getEdgeCrossing(1.0, lambda[3]);
    }
    if ((costFunctionConfig & COSTFUNCTION_EDGE_DISTANCE) != 0) {
        energy += getEdgeDistance(lambda[4]);
    }
    return energy;
}
