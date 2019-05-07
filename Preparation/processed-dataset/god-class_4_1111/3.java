/** Find the list of selected faces, and for each one,  calculate a displacement for each vertex. */
private void findVertexInsets(double height, double width) {
    Vertex v[] = (Vertex[]) mesh.getVertices();
    Face f[] = mesh.getFaces();
    double length, dot;
    Vec3 e1, e2, e3, normal;
    int i;
    faceInsets = new Vec3[f.length][];
    for (i = 0; i < f.length; i++) if (selected[i]) {
        faceInsets[i] = new Vec3[3];
        e1 = v[f[i].v2].r.minus(v[f[i].v1].r);
        e2 = v[f[i].v3].r.minus(v[f[i].v2].r);
        e3 = v[f[i].v1].r.minus(v[f[i].v3].r);
        e1.normalize();
        e2.normalize();
        e3.normalize();
        normal = e1.cross(e2);
        length = normal.length();
        if (length == 0.0)
            // A degenerate triangle. 
            faceInsets[i][0] = faceInsets[i][1] = faceInsets[i][2] = new Vec3();
        else {
            normal.scale(height / length);
            dot = -e1.dot(e3);
            faceInsets[i][0] = e1.minus(e3).times(width / Math.sqrt(1 - dot * dot)).plus(normal);
            dot = -e2.dot(e1);
            faceInsets[i][1] = e2.minus(e1).times(width / Math.sqrt(1 - dot * dot)).plus(normal);
            dot = -e3.dot(e2);
            faceInsets[i][2] = e3.minus(e2).times(width / Math.sqrt(1 - dot * dot)).plus(normal);
        }
    }
}
