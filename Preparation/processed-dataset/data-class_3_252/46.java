/**
	 * Scale the width and height of an image to a certain percentage.
	 *
	 * @param percentX
	 *            the scaling percentage of the width
	 * @param percentY
	 *            the scaling percentage of the height
	 */
public void scalePercent(float percentX, float percentY) {
    plainWidth = getWidth() * percentX / 100f;
    plainHeight = getHeight() * percentY / 100f;
    float[] matrix = matrix();
    scaledWidth = matrix[DX] - matrix[CX];
    scaledHeight = matrix[DY] - matrix[CY];
    setWidthPercentage(0);
}
