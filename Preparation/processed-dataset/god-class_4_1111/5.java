/** Find the list of selected faces, and for each one,  calculate a displacement for each edge. */
private void findEdgeInsets(double height, double width) {
    Vertex v[] = (Vertex[]) mesh.getVertices();
    Face f[] = mesh.getFaces();
    double length, dot;
    Vec3 e1, e2, e3;
    int i;
    faceInsets = new Vec3[f.length][];
    faceNormal = new Vec3[f.length];
    for (i = 0; i < f.length; i++) if (selected[i]) {
        faceInsets[i] = new Vec3[3];
        e1 = v[f[i].v2].r.minus(v[f[i].v1].r);
        e2 = v[f[i].v3].r.minus(v[f[i].v2].r);
        e3 = v[f[i].v1].r.minus(v[f[i].v3].r);
        e1.normalize();
        e2.normalize();
        e3.normalize();
        faceNormal[i] = e1.cross(e2);
        length = faceNormal[i].length();
        if (length == 0.0)
            // A degenerate triangle. 
            faceInsets[i][0] = faceInsets[i][1] = faceInsets[i][2] = faceNormal[i] = new Vec3();
        else {
            faceNormal[i].scale(1.0 / length);
            dot = -e1.dot(e2);
            faceInsets[i][0] = e2.plus(e1.times(dot));
            faceInsets[i][0].normalize();
            dot = -e2.dot(e3);
            faceInsets[i][1] = e3.plus(e2.times(dot));
            faceInsets[i][1].normalize();
            dot = -e3.dot(e1);
            faceInsets[i][2] = e1.plus(e3.times(dot));
            faceInsets[i][2].normalize();
        }
    }
}
