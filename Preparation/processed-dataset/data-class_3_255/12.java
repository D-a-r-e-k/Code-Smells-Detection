/**
     * Finds the intersection between the <CODE>yLine</CODE>,
     * the <CODE>yLine-leading</CODE>and the two column bounds.
     * It will set the <CODE>lineStatus</CODE> appropriately.
     *
     * @return a <CODE>float[4]</CODE>with the x coordinates of the intersection
     */
protected float[] findLimitsTwoLines() {
    boolean repeat = false;
    for (; ; ) {
        if (repeat && currentLeading == 0)
            return null;
        repeat = true;
        float x1[] = findLimitsOneLine();
        if (lineStatus == LINE_STATUS_OFFLIMITS)
            return null;
        yLine -= currentLeading;
        if (lineStatus == LINE_STATUS_NOLINE) {
            continue;
        }
        float x2[] = findLimitsOneLine();
        if (lineStatus == LINE_STATUS_OFFLIMITS)
            return null;
        if (lineStatus == LINE_STATUS_NOLINE) {
            yLine -= currentLeading;
            continue;
        }
        if (x1[0] >= x2[1] || x2[0] >= x1[1])
            continue;
        return new float[] { x1[0], x1[1], x2[0], x2[1] };
    }
}
