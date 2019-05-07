/******************************************************************************/
/**
 * Computes a random Vector with a random direction and a given length.
 */
public Point2D.Double getRandomVector(double maxLength) {
    double alpha = Math.random() * Math.PI * 2;
    double length = Math.random() * maxLength;
    return new Point2D.Double(length * Math.cos(alpha), length * Math.sin(alpha));
}
