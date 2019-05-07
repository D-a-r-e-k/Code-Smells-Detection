/**
	 * Sets the rotation of the image in degrees.
	 *
	 * @param deg
	 *            rotation in degrees
	 */
public void setRotationDegrees(float deg) {
    double d = Math.PI;
    setRotation(deg / 180 * (float) d);
}
