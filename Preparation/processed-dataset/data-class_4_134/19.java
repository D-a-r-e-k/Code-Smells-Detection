/******************************************************************************/
/**
 * Costfunction. One criterion for drawing a "nice" graph is to spread the cells
 * out evenly on the drawing space. The distances between the cells need not to 
 * be perfectly uniform, but the graph sould be occupy a reasonable part of
 * the drawing space, and, if possible, the cells shouldn't be overcrowded.
 * This function calculates the sum, over all pairs of cells, of a function
 * that is inverse-proportional to the distance between the cells.
 * 
 * @param lambda A normalizing factor that defines the relativ importance of
 * this criterion compared to others. Increasing lambda relative to the other
 * normalizing factors causes the Algorithm to prefer pictures with smaller 
 * distances between cells.
 * @return costs of this criterion
 */
private double getNodeDistribution(double lambda) {
    double energy = 0.0;
    for (int i = 0; i < applyCellList.size(); i++) for (int j = 0; j < cellList.size(); j++) {
        if (applyCellList.get(i) != cellList.get(j)) {
            double distance = MathExtensions.getEuclideanDistance(getPosition((CellView) applyCellList.get(i)), getPosition((CellView) cellList.get(j)));
            //prevents from dividing with Zero 
            if (Math.abs(distance) < equalsNull)
                distance = equalsNull;
            energy += lambda / (distance * distance);
        }
    }
    //        System.out.println("NodeDistribution : "+energy); 
    return energy;
}
