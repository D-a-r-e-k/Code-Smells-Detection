public RenderingMesh getRenderingMesh(double tol, boolean interactive, ObjectInfo info) {
    float us[], vs[];
    Vec3 point[], norm[];
    int i, j, k, u1, u2, v1, v2, udim, vdim, normIndex[][][];
    Vector normal;
    RenderingTriangle tri[];
    RenderingMesh mesh;
    if (interactive && cachedMesh != null)
        return cachedMesh;
    // First get the array of points. 
    SplineMesh newmesh = subdivideMesh(this, tol);
    us = newmesh.usmoothness;
    vs = newmesh.vsmoothness;
    cachedUSize = udim = newmesh.usize;
    cachedVSize = vdim = newmesh.vsize;
    point = new Vec3[newmesh.vertex.length];
    for (i = 0; i < point.length; i++) point[i] = newmesh.vertex[i].r;
    // Construct the list of normals. 
    normal = new Vector(point.length);
    normIndex = new int[udim][vdim][4];
    k = 0;
    for (i = 0; i < udim; i++) for (j = 0; j < vdim; j++) {
        u1 = i - 1;
        if (u1 == -1) {
            if (uclosed)
                u1 = udim - 1;
            else
                u1 = 0;
        }
        u2 = i + 1;
        if (u2 == udim) {
            if (uclosed)
                u2 = 0;
            else
                u2 = i;
        }
        v1 = j - 1;
        if (v1 == -1) {
            if (vclosed)
                v1 = vdim - 1;
            else
                v1 = 0;
        }
        v2 = j + 1;
        if (v2 == vdim) {
            if (vclosed)
                v2 = 0;
            else
                v2 = j;
        }
        if (us[i] < 1.0f && vs[j] < 1.0f) // Creases in both directions. 
        {
            normal.addElement(calcNormal(point, i, j, u1, i, v1, j, udim));
            normal.addElement(calcNormal(point, i, j, i, u2, v1, j, udim));
            normal.addElement(calcNormal(point, i, j, u1, i, j, v2, udim));
            normal.addElement(calcNormal(point, i, j, i, u2, j, v2, udim));
            normIndex[i][j][0] = k++;
            normIndex[i][j][1] = k++;
            normIndex[i][j][2] = k++;
            normIndex[i][j][3] = k++;
        } else if (us[i] < 1.0f) // Crease in the u direction. 
        {
            normal.addElement(calcNormal(point, i, j, u1, i, v1, v2, udim));
            normal.addElement(calcNormal(point, i, j, i, u2, v1, v2, udim));
            normIndex[i][j][0] = normIndex[i][j][2] = k++;
            normIndex[i][j][1] = normIndex[i][j][3] = k++;
        } else if (vs[j] < 1.0f) // Crease in the v direction. 
        {
            normal.addElement(calcNormal(point, i, j, u1, u2, v1, j, udim));
            normal.addElement(calcNormal(point, i, j, u1, u2, j, v2, udim));
            normIndex[i][j][0] = normIndex[i][j][1] = k++;
            normIndex[i][j][2] = normIndex[i][j][3] = k++;
        } else // Smooth vertex. 
        {
            normal.addElement(calcNormal(point, i, j, u1, u2, v1, v2, udim));
            normIndex[i][j][0] = normIndex[i][j][1] = normIndex[i][j][2] = normIndex[i][j][3] = k++;
        }
    }
    norm = new Vec3[normal.size()];
    for (i = 0; i < norm.length; i++) norm[i] = (Vec3) normal.elementAt(i);
    // Determine how many triangles there will be. 
    i = (udim - 1) * (vdim - 1);
    if (uclosed)
        i += vdim - 1;
    if (vclosed)
        i += udim - 1;
    if (uclosed && vclosed)
        i++;
    i *= 2;
    // Build the list of triangles. 
    tri = new RenderingTriangle[i];
    k = 0;
    for (i = 0; i < udim - 1; i++) for (j = 0; j < vdim - 1; j++) {
        tri[k++] = texMapping.mapTriangle(i + udim * j, i + 1 + udim * j, i + 1 + udim * (j + 1), normIndex[i][j][3], normIndex[i + 1][j][2], normIndex[i + 1][j + 1][0], point);
        tri[k++] = texMapping.mapTriangle(i + udim * j, i + 1 + udim * (j + 1), i + udim * (j + 1), normIndex[i][j][3], normIndex[i + 1][j + 1][0], normIndex[i][j + 1][1], point);
    }
    if (uclosed)
        for (i = 0; i < vdim - 1; i++) {
            tri[k++] = texMapping.mapTriangle((i + 1) * udim - 1, i * udim, (i + 1) * udim, normIndex[udim - 1][i][3], normIndex[0][i][2], normIndex[0][i + 1][0], point);
            tri[k++] = texMapping.mapTriangle((i + 1) * udim - 1, (i + 1) * udim, (i + 2) * udim - 1, normIndex[udim - 1][i][3], normIndex[0][i + 1][0], normIndex[udim - 1][i + 1][1], point);
        }
    if (vclosed)
        for (i = 0; i < udim - 1; i++) {
            tri[k++] = texMapping.mapTriangle(i + udim * (vdim - 1), i + 1 + udim * (vdim - 1), i + 1, normIndex[i][vdim - 1][3], normIndex[i + 1][vdim - 1][2], normIndex[i + 1][0][0], point);
            tri[k++] = texMapping.mapTriangle(i + udim * (vdim - 1), i + 1, i, normIndex[i][vdim - 1][3], normIndex[i + 1][0][0], normIndex[i][0][1], point);
        }
    if (uclosed && vclosed) {
        tri[k++] = texMapping.mapTriangle(udim * vdim - 1, udim * (vdim - 1), 0, normIndex[udim - 1][vdim - 1][3], normIndex[0][vdim - 1][2], normIndex[0][0][0], point);
        tri[k++] = texMapping.mapTriangle(udim * vdim - 1, 0, udim - 1, normIndex[udim - 1][vdim - 1][3], normIndex[0][0][0], normIndex[udim - 1][0][1], point);
    }
    mesh = new RenderingMesh(point, norm, tri, texMapping, matMapping);
    mesh.setParameters(newmesh.paramValue);
    if (interactive)
        cachedMesh = mesh;
    return mesh;
}
