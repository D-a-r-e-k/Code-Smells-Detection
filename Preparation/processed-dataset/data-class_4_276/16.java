/** This method rebuilds the mesh based on new lists of vertices and smoothness values. */
public void setShape(MeshVertex v[][], float usmoothness[], float vsmoothness[]) {
    int i, j;
    usize = v.length;
    vsize = v[0].length;
    vertex = new MeshVertex[usize * vsize];
    for (i = 0; i < usize; i++) for (j = 0; j < vsize; j++) vertex[i + usize * j] = v[i][j];
    this.usmoothness = usmoothness;
    this.vsmoothness = vsmoothness;
    cachedMesh = null;
    cachedWire = null;
    bounds = null;
}
