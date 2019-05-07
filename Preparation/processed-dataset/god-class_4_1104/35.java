public TriangleMesh convertToTriangleMesh(double tol) {
    int i, j, k, udim, vdim, faces[][];
    TriangleMesh trimesh;
    float us[], vs[];
    Vec3 point[];
    SplineMesh newmesh = subdivideMesh(this, tol);
    us = newmesh.usmoothness;
    vs = newmesh.vsmoothness;
    udim = newmesh.usize;
    vdim = newmesh.vsize;
    point = new Vec3[newmesh.vertex.length];
    for (i = 0; i < point.length; i++) point[i] = newmesh.vertex[i].r;
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
    faces = new int[i][];
    k = 0;
    for (i = 0; i < udim - 1; i++) for (j = 0; j < vdim - 1; j++) {
        faces[k++] = new int[] { i + udim * j, i + 1 + udim * j, i + 1 + udim * (j + 1) };
        faces[k++] = new int[] { i + udim * j, i + 1 + udim * (j + 1), i + udim * (j + 1) };
    }
    if (uclosed)
        for (i = 0; i < vdim - 1; i++) {
            faces[k++] = new int[] { (i + 1) * udim - 1, i * udim, (i + 1) * udim };
            faces[k++] = new int[] { (i + 1) * udim - 1, (i + 1) * udim, (i + 2) * udim - 1 };
        }
    if (vclosed)
        for (i = 0; i < udim - 1; i++) {
            faces[k++] = new int[] { i + udim * (vdim - 1), i + 1 + udim * (vdim - 1), i + 1 };
            faces[k++] = new int[] { i + udim * (vdim - 1), i + 1, i };
        }
    if (uclosed && vclosed) {
        faces[k++] = new int[] { udim * vdim - 1, udim * (vdim - 1), 0 };
        faces[k++] = new int[] { udim * vdim - 1, 0, udim - 1 };
    }
    trimesh = new TriangleMesh(point, faces);
    // Set the smoothness values for the edges of the triangle mesh. 
    TriangleMesh.Edge ed[] = trimesh.getEdges();
    for (i = 0; i < ed.length; i++) {
        j = ed[i].v1 / udim;
        k = ed[i].v2 / udim;
        if (j == k)
            ed[i].smoothness = vs[j];
        else {
            j = ed[i].v1 % udim;
            ed[i].smoothness = us[j];
        }
    }
    // Copy over the texture, texture parameters, and material. 
    trimesh.copyTextureAndMaterial(newmesh);
    return trimesh;
}
