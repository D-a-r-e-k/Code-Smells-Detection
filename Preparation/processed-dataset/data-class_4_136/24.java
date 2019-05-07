/******************************************************************************/
/**
 * Creates a random Vector, with a given length and a random direction.
 * 
 * @param length Length of the Vector created by this method
 * @return Vector represented by a Point2D.Double
 */
private Point2D.Double getRandomVector(double length) {
    double alpha = Math.random() * Math.PI * 2;
    //        double length = Math.random()*maxLength; 
    return new Point2D.Double(length * Math.cos(alpha), length * Math.sin(alpha));
}
