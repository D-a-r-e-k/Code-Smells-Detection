/******************************************************************************/
/**
 * Costfunction. As in physics, truly minimizing the potential energy might
 * result in spreading out the elements indefinitely. To avoid this, and to 
 * reflect the physical limitations of the output device, add this costfunction
 * to the energy function to deal with the borderlines of the drawing space.
 * 
 * @param lambda Value relative to the other lamdas pushes the cells 
 * towards the center, while decreasing it results in using more of the
 * drawing space near the borderlines. 
 * @return costs of this criterion
 */
private double getBorderline(double lambda) {
    double energy = 0.0;
    for (int i = 0; i < applyCellList.size(); i++) {
        Point2D.Double pos = getPosition((CellView) applyCellList.get(i));
        double t = pos.y - bounds.y;
        double l = pos.x - bounds.x;
        double b = bounds.y + bounds.height - pos.y;
        double r = bounds.x + bounds.width - pos.x;
        energy += lambda * ((1.0 / (t * t)) + (1.0 / (l * l)) + (1.0 / (b * b)) + (1.0 / (r * r)));
    }
    //        System.out.println("Borderline       : "+energy); 
    return energy;
}
