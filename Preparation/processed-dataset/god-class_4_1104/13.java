/** Set the positions for all the vertices of the mesh. */
public void setVertexPositions(Vec3 v[]) {
    for (int i = 0; i < v.length; i++) vertex[i].r = v[i];
    cachedMesh = null;
    cachedWire = null;
    bounds = null;
}
