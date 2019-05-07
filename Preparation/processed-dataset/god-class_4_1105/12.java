/** This method is called recursively to build the packed kd-tree of photons from the workspace array.
      @param start      the start of the segment from which to build the tree
      @param end        the end of the segment from which to build the tree
      @param root       the position in the packed array where the root of the tree should go
  */
private void buildTree(int start, int end, int root) {
    if (start == end)
        photon[root] = workspace[start];
    if (start >= end)
        return;
    // Find a bounding box for the photons in this segment, and decide which axis to split. 
    float minx = Float.MAX_VALUE, miny = Float.MAX_VALUE, minz = Float.MAX_VALUE;
    float maxx = -Float.MAX_VALUE, maxy = -Float.MAX_VALUE, maxz = -Float.MAX_VALUE;
    for (int i = start; i <= end; i++) {
        Photon p = workspace[i];
        if (p.x < minx)
            minx = p.x;
        if (p.y < miny)
            miny = p.y;
        if (p.z < minz)
            minz = p.z;
        if (p.x > maxx)
            maxx = p.x;
        if (p.y > maxy)
            maxy = p.y;
        if (p.z > maxz)
            maxz = p.z;
    }
    float xsize = maxx - minx, ysize = maxy - miny, zsize = maxz - minz;
    int axis;
    if (xsize > ysize && xsize > zsize)
        axis = 0;
    else if (ysize > zsize)
        axis = 1;
    else
        axis = 2;
    // Split the photons about the median along this axis. 
    int size = end - start + 1;
    int medianPos = 1;
    while (4 * medianPos <= size) medianPos += medianPos;
    if (3 * medianPos <= size)
        medianPos = 2 * medianPos + start - 1;
    else
        medianPos = end - medianPos + 1;
    medianSplit(start, end, medianPos, axis);
    // Store the median photon, and build the subtrees. 
    photon[root] = workspace[medianPos];
    photon[root].axis = (short) axis;
    buildTree(start, medianPos - 1, 2 * root + 1);
    buildTree(medianPos + 1, end, 2 * root + 2);
}
