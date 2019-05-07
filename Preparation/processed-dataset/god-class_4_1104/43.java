/** Reverse the points along one direction.  This will cause all the normal vectors to
     be flipped. */
public void reverseOrientation() {
    MeshVertex swapVert;
    float swapSmooth;
    int i, j;
    for (i = 0; i < usize / 2; i++) {
        for (j = 0; j < vsize; j++) {
            swapVert = vertex[i + usize * j];
            vertex[i + usize * j] = vertex[usize - 1 - i + usize * j];
            vertex[usize - 1 - i + usize * j] = swapVert;
        }
        swapSmooth = usmoothness[i];
        usmoothness[i] = usmoothness[usize - 1 - i];
        usmoothness[usize - 1 - i] = swapSmooth;
    }
    cachedMesh = null;
}
