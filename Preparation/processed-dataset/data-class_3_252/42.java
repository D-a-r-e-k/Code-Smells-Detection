/**
	 * Scale the image to an absolute width and an absolute height.
	 *
	 * @param newWidth
	 *            the new width
	 * @param newHeight
	 *            the new height
	 */
public void scaleAbsolute(float newWidth, float newHeight) {
    plainWidth = newWidth;
    plainHeight = newHeight;
    float[] matrix = matrix();
    scaledWidth = matrix[DX] - matrix[CX];
    scaledHeight = matrix[DY] - matrix[CY];
    setWidthPercentage(0);
}
