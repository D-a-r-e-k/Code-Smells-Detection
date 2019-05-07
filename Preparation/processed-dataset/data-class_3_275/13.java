/** Subdivide the curve which defines this tube to the specified tolerance. */
private Tube subdivideTubeInterp(double tol) {
    Tube t = this;
    MeshVertex newvert[];
    float news[];
    int i, j, p1, p2, p3, p4, count;
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
    if (t.closed)
        refine = new boolean[t.vertex.length];
    else
        refine = new boolean[t.vertex.length - 1];
    for (i = 0; i < refine.length; i++) refine[i] = true;
    count = refine.length;
    int iterations = 0;
    do {
        int len = t.vertex.length;
        newvert = new MeshVertex[len + count];
        news = new float[len + count];
        newt = new double[len + count];
        newparam = new double[len + count][numParam];
        newrefine = new boolean[len + count];
        for (i = 0, j = 0; j < len; j++) {
            // Copy over the existing vertex. 
            newvert[i] = t.vertex[j];
            newt[i] = t.thickness[j];
            news[i] = t.smoothness[j] * 2.0f;
            if (news[i] > 1.0f)
                news[i] = 1.0f;
            newparam[i] = param[j];
            i++;
            if (j < refine.length && refine[j]) {
                // Create the interpolated vertex. 
                p1 = j - 1;
                if (p1 < 0)
                    p1 = (t.closed ? len - 1 : 0);
                p2 = j;
                p3 = j + 1;
                if (p3 >= len)
                    p3 = (t.closed ? p3 - len : len - 1);
                p4 = j + 2;
                if (p4 >= len)
                    p4 = (t.closed ? p4 - len : len - 1);
                newvert[i] = SplineMesh.calcInterpPoint(t.vertex, t.smoothness, param, paramTemp, p1, p2, p3, p4);
                newt[i] = calcInterpThickness(t.thickness, t.smoothness, p1, p2, p3, p4);
                news[i] = 1.0f;
                for (int k = 0; k < numParam; k++) newparam[i][k] = paramTemp[k];
                if (newvert[i].r.distance2(t.vertex[p2].r) > tol2 && newvert[i].r.distance2(t.vertex[p3].r) > tol2) {
                    Vec3 temp = t.vertex[p2].r.plus(t.vertex[p3].r).times(0.5);
                    if (temp.distance2(newvert[i].r) > tol2) {
                        newrefine[i] = true;
                        if (i > 0)
                            newrefine[i - 1] = true;
                    }
                }
                i++;
            }
        }
        count = 0;
        for (j = 0; j < newrefine.length; j++) if (newrefine[j])
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
