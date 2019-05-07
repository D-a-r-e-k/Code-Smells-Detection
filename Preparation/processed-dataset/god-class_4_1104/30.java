public static MeshVertex calcApproxPoint(MeshVertex v[], float s[], double oldParam[][], double newParam[], int i, int j, int k) {
    double w1 = 0.125 * s[j], w2 = 1.0 - 2.0 * w1;
    MeshVertex vt = new MeshVertex(new Vec3(w1 * v[i].r.x + w2 * v[j].r.x + w1 * v[k].r.x, w1 * v[i].r.y + w2 * v[j].r.y + w1 * v[k].r.y, w1 * v[i].r.z + w2 * v[j].r.z + w1 * v[k].r.z));
    for (int n = 0; n < newParam.length; n++) newParam[n] = w1 * oldParam[i][n] + w2 * oldParam[j][n] + w1 * oldParam[k][n];
    vt.ikJoint = v[j].ikJoint;
    vt.ikWeight = v[j].ikWeight;
    return vt;
}
