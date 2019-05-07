/**
     * Simplified method for rectangular columns.
     *
     * @param llx
     * @param lly
     * @param urx
     * @param ury
     */
public void setSimpleColumn(float llx, float lly, float urx, float ury) {
    leftX = Math.min(llx, urx);
    maxY = Math.max(lly, ury);
    minY = Math.min(lly, ury);
    rightX = Math.max(llx, urx);
    yLine = maxY;
    rectangularWidth = rightX - leftX;
    if (rectangularWidth < 0)
        rectangularWidth = 0;
    rectangularMode = true;
}
