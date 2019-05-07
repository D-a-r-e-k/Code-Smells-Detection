/** Given one vertex, create another one which is offset from it. */
private Vertex offsetVertex(TriangleMesh mesh, Vertex v, Vec3 offset) {
    Vertex vert = mesh.new Vertex(v);
    vert.r.add(offset);
    vert.edges = 0;
    vert.firstEdge = -1;
    return vert;
}
