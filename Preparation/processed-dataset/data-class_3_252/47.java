/**
	 * Scales the image so that it fits a certain width and height.
	 *
	 * @param fitWidth
	 *            the width to fit
	 * @param fitHeight
	 *            the height to fit
	 */
public void scaleToFit(float fitWidth, float fitHeight) {
    scalePercent(100);
    float percentX = fitWidth * 100 / getScaledWidth();
    float percentY = fitHeight * 100 / getScaledHeight();
    scalePercent(percentX < percentY ? percentX : percentY);
    setWidthPercentage(0);
}
