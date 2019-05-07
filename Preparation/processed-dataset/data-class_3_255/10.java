/**
     * Finds the intersection between the <CODE>yLine</CODE> and the column. It will
     * set the <CODE>lineStatus</CODE> appropriately.
     *
     * @param wall the column to intersect
     * @return the x coordinate of the intersection
     */
protected float findLimitsPoint(ArrayList<float[]> wall) {
    lineStatus = LINE_STATUS_OK;
    if (yLine < minY || yLine > maxY) {
        lineStatus = LINE_STATUS_OFFLIMITS;
        return 0;
    }
    for (int k = 0; k < wall.size(); ++k) {
        float r[] = wall.get(k);
        if (yLine < r[0] || yLine > r[1])
            continue;
        return r[2] * yLine + r[3];
    }
    lineStatus = LINE_STATUS_NOLINE;
    return 0;
}
