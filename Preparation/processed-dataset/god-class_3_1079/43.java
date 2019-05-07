/**
	 * Scale the image to an absolute width.
	 *
	 * @param newWidth
	 *            the new width
	 */
public void scaleAbsoluteWidth(float newWidth) {
    plainWidth = newWidth;
    float[] matrix = matrix();
    scaledWidth = matrix[DX] - matrix[CX];
    scaledHeight = matrix[DY] - matrix[CY];
    setWidthPercentage(0);
}
