/**
	 * Scale the image to an absolute height.
	 *
	 * @param newHeight
	 *            the new height
	 */
public void scaleAbsoluteHeight(float newHeight) {
    plainHeight = newHeight;
    float[] matrix = matrix();
    scaledWidth = matrix[DX] - matrix[CX];
    scaledHeight = matrix[DY] - matrix[CY];
    setWidthPercentage(0);
}
