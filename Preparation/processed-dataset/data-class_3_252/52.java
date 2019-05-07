/**
	 * Sets the rotation of the image in radians.
	 *
	 * @param r
	 *            rotation in radians
	 */
public void setRotation(float r) {
    double d = 2.0 * Math.PI;
    rotationRadians = (float) ((r + initialRotation) % d);
    if (rotationRadians < 0) {
        rotationRadians += d;
    }
    float[] matrix = matrix();
    scaledWidth = matrix[DX] - matrix[CX];
    scaledHeight = matrix[DY] - matrix[CY];
}
