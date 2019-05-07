/**
     * Finds the intersection between the <CODE>yLine</CODE> and the two
     * column bounds. It will set the <CODE>lineStatus</CODE> appropriately.
     *
     * @return a <CODE>float[2]</CODE>with the x coordinates of the intersection
     */
protected float[] findLimitsOneLine() {
    float x1 = findLimitsPoint(leftWall);
    if (lineStatus == LINE_STATUS_OFFLIMITS || lineStatus == LINE_STATUS_NOLINE)
        return null;
    float x2 = findLimitsPoint(rightWall);
    if (lineStatus == LINE_STATUS_NOLINE)
        return null;
    return new float[] { x1, x2 };
}
