/** Subdivide the curve which defines this tube to the specified tolerance. */
private Tube subdivideTubeApprox(double tol) {
    Tube t = this;
    MeshVertex newvert[];
    float news[];
    int i, j, p1, p2, p3, count;
    int numParam = (texParam == null ? 0 : texParam.length);
    double newt[], param[][], newparam[][], paramTemp[] = new double[numParam], tol2 = tol * tol;
    boolean refine[], newrefine[];
    param = new double[t.vertex.length][numParam];
    for (i = 0; i < numParam; i++) {
        if (paramValue[i] instanceof VertexParameterValue) {
            double val[] = ((VertexParameterValue) paramValue[i]).getValue();
            for (j = 0; j < val.length; j++) param[j][i] = val[j];
        }
    }
    refine = new boolean[t.vertex.length];
    if (t.closed) {
        for (i = 0; i < refine.length; i++) refine[i] = true;
        count = refine.length;
    } else {
        for (i = 1; i < refine.length - 1; i++) refine[i] = true;
        count = refine.length - 1;
    }
    int iterations = 0;
    do {
        int len = t.vertex.length;
        newvert = new MeshVertex[len + count];
        news = new float[len + count];
        newt = new double[len + count];
        newparam = new double[len + count][numParam];
        newrefine = new boolean[len + count];
        for (i = 0, j = 0; j < len; j++) {
            p1 = j - 1;
            if (p1 < 0)
                p1 = (t.closed ? len - 1 : 0);
            p2 = j;
            p3 = j + 1;
            if (p3 >= len)
                p3 = (t.closed ? p3 - len : len - 1);
            if (!refine[j]) {
                // Copy over the existing vertex. 
                newvert[i] = t.vertex[j];
                newt[i] = t.thickness[j];
                news[i] = t.smoothness[j];
                newparam[i] = param[j];
            } else {
                // Find the new position for the vertex. 
                newvert[i] = SplineMesh.calcApproxPoint(t.vertex, t.smoothness, param, paramTemp, p1, p2, p3);
                newt[i] = calcApproxThickness(t.thickness, t.smoothness, p1, p2, p3);
                news[i] = t.smoothness[j] * 2.0f;
                if (news[i] > 1.0f)
                    news[i] = 1.0f;
                for (int k = 0; k < numParam; k++) newparam[i][k] = paramTemp[k];
            }
            i++;
            if (!refine[p2] && !refine[p3])
                continue;
            // Add a new vertex. 
            newvert[i] = MeshVertex.blend(t.vertex[p2], t.vertex[p3], 0.5, 0.5);
            newt[i] = 0.5 * (t.thickness[p2] + t.thickness[p3]);
            news[i] = 1.0f;
            for (int k = 0; k < numParam; k++) newparam[i][k] = 0.5 * (param[p2][k] + param[p3][k]);
            // Decide whether we need to subdivide further. 
            if (newvert[i - 1].r.distance2(t.vertex[j].r) > tol2) {
                if (newvert[i].r.distance2(newvert[i - 1].r) > tol2 && (i < 2 || newvert[i - 1].r.distance2(newvert[i - 2].r) > tol2)) {
                    newrefine[i] = newrefine[i - 1] = true;
                    if (i > 1)
                        newrefine[i - 2] = true;
                }
            }
            i++;
        }
        count = 0;
        for (j = 0; j < newrefine.length - 1; j++) if (newrefine[j] || newrefine[j + 1])
            count++;
        if (t.closed && (newrefine[newrefine.length - 1] || newrefine[0]))
            count++;
        t = new Tube(newvert, news, newt, t.smoothingMethod, t.endsStyle);
        param = newparam;
        refine = newrefine;
        iterations++;
    } while (count > 0 && iterations < MAX_SUBDIVISIONS);
    t.copyTextureAndMaterial(this);
    for (i = 0; i < numParam; i++) {
        if (paramValue[i] instanceof VertexParameterValue) {
            double val[] = new double[t.vertex.length];
            for (j = 0; j < val.length; j++) val[j] = param[j][i];
            t.paramValue[i] = new VertexParameterValue(val);
        }
    }
    return t;
}
