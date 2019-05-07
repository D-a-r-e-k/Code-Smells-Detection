/**
     * Sets the columns bounds. Each column bound is described by a
     * <CODE>float[]</CODE> with the line points [x1,y1,x2,y2,...].
     * The array must have at least 4 elements.
     *
     * @param leftLine the left column bound
     * @param rightLine the right column bound
     */
public void setColumns(float leftLine[], float rightLine[]) {
    maxY = -10e20f;
    minY = 10e20f;
    setYLine(Math.max(leftLine[1], leftLine[leftLine.length - 1]));
    rightWall = convertColumn(rightLine);
    leftWall = convertColumn(leftLine);
    rectangularWidth = -1;
    rectangularMode = false;
}
