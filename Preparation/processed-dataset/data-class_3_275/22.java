/** This is a utility routine used by both getRenderingMesh() and convertToTriangleMesh().
      It subdivides the surface and fills in the vectors with lists of vertices, normals,
      faces, and parameter values. */
private void subdivideSurface(double tol, Vector vert, Vector norm, Vector face, Vector param) {
    // Subdivide the central curve to the desired tolerance. 
    Tube t = subdivideTube(tol);
    Vec3 pathv[] = new Vec3[t.vertex.length];
    for (int i = 0; i < pathv.length; i++) pathv[i] = t.vertex[i].r;
    int numParam = (texParam == null ? 0 : texParam.length);
    double tubeParamVal[][] = new double[t.vertex.length][numParam];
    for (int i = 0; i < numParam; i++) {
        if (t.paramValue[i] instanceof VertexParameterValue) {
            double val[] = ((VertexParameterValue) t.paramValue[i]).getValue();
            for (int j = 0; j < tubeParamVal.length; j++) tubeParamVal[j][i] = val[j];
        } else {
            double val = t.paramValue[i].getAverageValue();
            for (int j = 0; j < tubeParamVal.length; j++) tubeParamVal[j][i] = val;
        }
    }
    // Figure out how many subdivisions we need around the circumference. 
    double max = 0.0;
    for (int i = 0; i < t.thickness.length; i++) if (t.thickness[i] > max)
        max = t.thickness[i];
    double r = 0.7 * max;
    // really 0.5, but include a fudge factor 
    int n = 0;
    if (r > tol)
        n = (int) Math.ceil(Math.PI / (Math.acos(1.0 - tol / r)));
    if (n < 3)
        n = 3;
    // Construct the Minimally Rotating Frame at every point along the path.  First,  
    // subdivide the path and determine its direction at the starting point. 
    Vec3 subdiv[], zdir[], updir[], xdir[];
    subdiv = new Curve(pathv, t.smoothness, t.getSmoothingMethod(), t.closed).subdivideCurve().getVertexPositions();
    xdir = new Vec3[subdiv.length];
    zdir = new Vec3[subdiv.length];
    updir = new Vec3[subdiv.length];
    if (t.closed)
        xdir[0] = subdiv[1].minus(subdiv[subdiv.length - 1]);
    else
        xdir[0] = subdiv[1].minus(subdiv[0]);
    xdir[0].normalize();
    if (Math.abs(xdir[0].y) > Math.abs(xdir[0].z))
        zdir[0] = xdir[0].cross(Vec3.vz());
    else
        zdir[0] = xdir[0].cross(Vec3.vy());
    zdir[0].normalize();
    updir[0] = xdir[0].cross(zdir[0]);
    // Now find two vectors perpendicular to the path, and determine how much they 
    // contribute to the z and up directions. 
    Vec3 dir1, dir2;
    double zfrac1, zfrac2, upfrac1, upfrac2;
    zfrac1 = xdir[0].dot(zdir[0]);
    zfrac2 = Math.sqrt(1.0 - zfrac1 * zfrac1);
    dir1 = zdir[0].minus(xdir[0].times(zfrac1));
    dir1.normalize();
    upfrac1 = xdir[0].dot(updir[0]);
    upfrac2 = Math.sqrt(1.0 - upfrac1 * upfrac1);
    dir2 = updir[0].minus(xdir[0].times(upfrac1));
    dir2.normalize();
    // Propagate the vectors along the path. 
    for (int i = 1; i < subdiv.length; i++) {
        if (i == subdiv.length - 1) {
            if (t.closed)
                xdir[i] = subdiv[0].minus(subdiv[subdiv.length - 2]);
            else
                xdir[i] = subdiv[subdiv.length - 1].minus(subdiv[subdiv.length - 2]);
        } else
            xdir[i] = subdiv[i + 1].minus(subdiv[i - 1]);
        xdir[i].normalize();
        dir1 = dir1.minus(xdir[i].times(xdir[i].dot(dir1)));
        dir1.normalize();
        dir2 = dir2.minus(xdir[i].times(xdir[i].dot(dir2)));
        dir2.normalize();
        zdir[i] = xdir[i].times(zfrac1).plus(dir1.times(zfrac2));
        updir[i] = xdir[i].cross(zdir[i]);
        updir[i].normalize();
    }
    // Now calculate the vertices for the sides of the tube. 
    double dtheta = 2.0 * Math.PI / n, theta = 0.0;
    for (int i = 0; i < pathv.length; i++) {
        int k = (pathv.length == subdiv.length ? i : 2 * i);
        Vec3 orig = pathv[i], z = zdir[k], up = updir[k];
        r = 0.5 * t.thickness[i];
        for (int j = 0; j < n; j++) {
            double sin = Math.sin(theta), cos = Math.cos(theta);
            Vec3 normal = new Vec3(cos * z.x + sin * up.x, cos * z.y + sin * up.y, cos * z.z + sin * up.z);
            norm.addElement(normal);
            MeshVertex mv = new MeshVertex(new Vec3(orig.x + r * normal.x, orig.y + r * normal.y, orig.z + r * normal.z));
            vert.addElement(mv);
            param.addElement(tubeParamVal[i]);
            theta += dtheta;
        }
    }
    // Create the faces for the sides of the tube. 
    for (int i = 0; i < pathv.length - 1; i++) {
        int k = i * n;
        for (int j = 0; j < n - 1; j++) {
            face.addElement(new int[] { k + j, k + j + 1, k + j + n });
            face.addElement(new int[] { k + j + 1, k + j + n + 1, k + j + n });
        }
        face.addElement(new int[] { k + n - 1, k, k + n + n - 1 });
        face.addElement(new int[] { k, k + n, k + n + n - 1 });
    }
    // Handle the ends appropriately. 
    if (endsStyle == CLOSED_ENDS) {
        // Connect the ends together. 
        int k = (pathv.length - 1) * n;
        for (int j = 0; j < n - 1; j++) {
            face.addElement(new int[] { k + j, k + j + 1, j });
            face.addElement(new int[] { k + j + 1, j + 1, j });
        }
        face.addElement(new int[] { k + n - 1, k, n - 1 });
        face.addElement(new int[] { k, 0, n - 1 });
    } else if (endsStyle == FLAT_ENDS) {
        // Create flat caps covering the ends. 
        int k = vert.size();
        vert.addElement(new MeshVertex(t.vertex[0]));
        vert.addElement(new MeshVertex(t.vertex[t.vertex.length - 1]));
        param.addElement(tubeParamVal[0]);
        param.addElement(tubeParamVal[t.vertex.length - 1]);
        for (int i = 0; i < n - 1; i++) face.addElement(new int[] { i + 1, i, k });
        face.addElement(new int[] { 0, n - 1, k });
        k++;
        int j = n * (pathv.length - 1);
        for (int i = 0; i < n - 1; i++) face.addElement(new int[] { j + i, j + i + 1, k });
        face.addElement(new int[] { j + n - 1, j, k });
    }
}
