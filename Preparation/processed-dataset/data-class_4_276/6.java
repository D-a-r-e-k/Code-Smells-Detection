/** Get a single vertex. */
public final MeshVertex getVertex(int u, int v) {
    return vertex[u + usize * v];
}
