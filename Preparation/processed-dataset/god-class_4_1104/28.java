/** This is called by subdivideMesh().  It takes a matrix of points and performs approximating
     subdivision along the second dimension.  It returns an array containing three elements:
     the subdivided matrix, the new list of smoothness values, and the new list of parameter values. */
private static Object[] approxOneAxis(MeshVertex v[][], float s[], double param[][][], boolean closed, double tol) {
    boolean refine[], newrefine[];
    float news[];
    int numParam = param[0][0].length;
    double paramTemp[] = new double[numParam], newparam[][][];
    Vec3 temp;
    MeshVertex newv[][];
    int i, j, k, count, p1, p3;
    refine = new boolean[v[0].length];
    for (i = 0; i < refine.length; i++) refine[i] = true;
    if (closed)
        count = refine.length;
    else {
        count = refine.length - 1;
        refine[0] = refine[refine.length - 1] = false;
    }
    int iterations = 0;
    do {
        newrefine = new boolean[refine.length + count];
        newv = new MeshVertex[v.length][v[0].length + count];
        news = new float[v[0].length + count];
        newparam = new double[v.length][v[0].length + count][numParam];
        for (i = 0, k = 0; i < refine.length; i++) {
            p1 = i - 1;
            if (p1 < 0) {
                if (closed)
                    p1 = refine.length - 1;
                else
                    p1 = 0;
            }
            p3 = i + 1;
            if (p3 == refine.length) {
                if (closed)
                    p3 = 0;
                else
                    p3 = refine.length - 1;
            }
            // Calculate the new positions for existing points. 
            if (!refine[i])
                for (j = 0; j < v.length; j++) {
                    newv[j][k] = v[j][i];
                    newparam[j][k] = param[j][i];
                }
            else
                for (j = 0; j < v.length; j++) {
                    newv[j][k] = calcApproxPoint(v[j], s, param[j], paramTemp, p1, i, p3);
                    for (int m = 0; m < numParam; m++) newparam[j][k][m] = paramTemp[m];
                    temp = newv[j][k].r.minus(v[j][i].r);
                    if (temp.length2() > tol * tol)
                        newrefine[k] = newrefine[(k - 1 + newrefine.length) % newrefine.length] = newrefine[(k + 1) % newrefine.length] = true;
                }
            news[k] = Math.min(s[i] * 2.0f, 1.0f);
            if (!closed && i == refine.length - 1)
                break;
            // Now calculate positions for the new points. 
            k++;
            if (refine[i] || refine[p3]) {
                for (j = 0; j < v.length; j++) {
                    newv[j][k] = MeshVertex.blend(v[j][i], v[j][p3], 0.5, 0.5);
                    for (int m = 0; m < numParam; m++) newparam[j][k][m] = 0.5 * (param[j][i][m] + param[j][p3][m]);
                }
                news[k] = 1.0f;
                k++;
            }
        }
        // Count the number of rows which are not yet converged. 
        count = 0;
        for (j = 0; j < newrefine.length - 1; j++) if (newrefine[j] || newrefine[j + 1])
            count++;
        if (closed)
            if (newrefine[0] || newrefine[newrefine.length - 1])
                count++;
        v = newv;
        s = news;
        param = newparam;
        refine = newrefine;
    } while (count > 0 && ++iterations < MAX_SUBDIVISIONS);
    return new Object[] { v, s, param };
}
