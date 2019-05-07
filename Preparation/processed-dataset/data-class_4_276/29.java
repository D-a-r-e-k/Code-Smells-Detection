/** The following two routines are used by subdivideMesh to calculate new point positions 
     for interpolating and approximating subdivision.  v is an array of vertices, s is
     the array of smoothness values for them, and i, j, k, and m are the indices of the points
     from which the new point will be calculated. */
public static MeshVertex calcInterpPoint(MeshVertex v[], float s[], double oldParam[][], double newParam[], int i, int j, int k, int m) {
    double w1, w2, w3, w4;
    w1 = -0.0625 * s[j];
    w2 = 0.5 - w1;
    w4 = -0.0625 * s[k];
    w3 = 0.5 - w4;
    MeshVertex vt = new MeshVertex(new Vec3(w1 * v[i].r.x + w2 * v[j].r.x + w3 * v[k].r.x + w4 * v[m].r.x, w1 * v[i].r.y + w2 * v[j].r.y + w3 * v[k].r.y + w4 * v[m].r.y, w1 * v[i].r.z + w2 * v[j].r.z + w3 * v[k].r.z + w4 * v[m].r.z));
    for (int n = 0; n < newParam.length; n++) newParam[n] = w1 * oldParam[i][n] + w2 * oldParam[j][n] + w3 * oldParam[k][n] + w4 * oldParam[m][n];
    if (v[j].ikJoint == v[k].ikJoint) {
        vt.ikJoint = v[j].ikJoint;
        vt.ikWeight = 0.5 * (v[j].ikWeight + v[k].ikWeight);
    } else if (v[j].ikWeight > v[k].ikWeight) {
        vt.ikJoint = v[j].ikJoint;
        vt.ikWeight = v[j].ikWeight;
    } else {
        vt.ikJoint = v[k].ikJoint;
        vt.ikWeight = v[k].ikWeight;
    }
    return vt;
}
