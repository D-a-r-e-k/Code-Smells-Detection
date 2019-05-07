/** This is called by subdivideMesh().  It takes a matrix of points and performs interpolating
     subdivision along the second dimension.  It returns an array containing three elements:
     the subdivided matrix, the new list of smoothness values, and the new list of parameter values. */
private static Object[] interpOneAxis(MeshVertex v[][], float s[], double param[][][], boolean closed, double tol) {
    boolean refine[], newrefine[];
    float news[];
    int numParam = param[0][0].length;
    double paramTemp[] = new double[numParam], newparam[][][];
    Vec3 temp;
    MeshVertex newv[][];
    int i, j, k, count, p1, p3, p4;
    double tol2 = tol * tol;
    if (closed)
        refine = new boolean[v[0].length];
    else
        refine = new boolean[v[0].length - 1];
    for (i = 0; i < refine.length; i++) refine[i] = true;
    count = refine.length;
    int iterations = 0;
    do {
        newrefine = new boolean[refine.length + count];
        newv = new MeshVertex[v.length][v[0].length + count];
        news = new float[v[0].length + count];
        newparam = new double[v.length][v[0].length + count][numParam];
        for (i = 0, k = 0; i < refine.length; i++) {
            // Existing points remain unchanged. 
            for (j = 0; j < v.length; j++) {
                newv[j][k] = v[j][i];
                newparam[j][k] = param[j][i];
            }
            news[k] = Math.min(s[i] * 2.0f, 1.0f);
            // Now calculate positions for the new points. 
            k++;
            if (refine[i]) {
                p1 = i - 1;
                if (p1 < 0) {
                    if (closed)
                        p1 = v[0].length - 1;
                    else
                        p1 = 0;
                }
                p3 = i + 1;
                if (p3 == v[0].length) {
                    if (closed)
                        p3 = 0;
                    else
                        p3 = v[0].length - 1;
                }
                p4 = i + 2;
                if (p4 >= v[0].length) {
                    if (closed)
                        p4 %= v[0].length;
                    else
                        p4 = v[0].length - 1;
                }
                for (j = 0; j < v.length; j++) {
                    newv[j][k] = calcInterpPoint(v[j], s, param[j], paramTemp, p1, i, p3, p4);
                    for (int m = 0; m < numParam; m++) newparam[j][k][m] = paramTemp[m];
                    if (v[j][i].r.distance2(newv[j][k].r) > tol2 && v[j][p3].r.distance2(newv[j][k].r) > tol2) {
                        temp = v[j][i].r.plus(v[j][p3].r).times(0.5);
                        if (temp.distance2(newv[j][k].r) > tol2)
                            newrefine[k] = newrefine[(k - 1 + newrefine.length) % newrefine.length] = true;
                    }
                }
                news[k] = 1.0f;
                k++;
            }
        }
        if (!closed)
            for (j = 0; j < v.length; j++) {
                newv[j][k] = v[j][i];
                newparam[j][k] = param[j][i];
            }
        // Count the number of rows which are not yet converged. 
        count = 0;
        for (j = 0; j < newrefine.length; j++) if (newrefine[j])
            count++;
        v = newv;
        s = news;
        param = newparam;
        refine = newrefine;
    } while (count > 0 && ++iterations < MAX_SUBDIVISIONS);
    return new Object[] { v, s, param };
}
