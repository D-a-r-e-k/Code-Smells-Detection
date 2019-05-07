/** Get a list of the positions of all vertices which define the mesh. */
public Vec3[] getVertexPositions() {
    Vec3 v[] = new Vec3[vertex.length];
    for (int i = 0; i < v.length; i++) v[i] = new Vec3(vertex[i].r);
    return v;
}
