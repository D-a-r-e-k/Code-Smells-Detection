public WireframeMesh getWireframeMesh() {
    Vec3 point[];
    int i, j, k, udim, vdim, from[], to[];
    if (cachedWire != null)
        return cachedWire;
    // First get the array of points. 
    if (cachedMesh != null) {
        point = cachedMesh.vert;
        udim = cachedUSize;
        vdim = cachedVSize;
    } else {
        SplineMesh newmesh = subdivideMesh(this, ArtOfIllusion.getPreferences().getInteractiveSurfaceError());
        cachedUSize = udim = newmesh.usize;
        cachedVSize = vdim = newmesh.vsize;
        point = new Vec3[newmesh.vertex.length];
        for (i = 0; i < point.length; i++) point[i] = newmesh.vertex[i].r;
    }
    // Determine how many lines there will be. 
    i = udim * (vdim - 1) + vdim * (udim - 1);
    if (uclosed)
        i += vdim;
    if (vclosed)
        i += udim;
    // Build the list of lines. 
    from = new int[i];
    to = new int[i];
    k = 0;
    for (i = 0; i < udim - 1; i++) for (j = 0; j < vdim - 1; j++) {
        from[k] = from[k + 1] = i + udim * j;
        to[k++] = i + 1 + udim * j;
        to[k++] = i + udim * (j + 1);
    }
    for (i = 0; i < udim - 1; i++) {
        from[k] = i + udim * (vdim - 1);
        to[k++] = i + 1 + udim * (vdim - 1);
    }
    for (i = 0; i < vdim - 1; i++) {
        from[k] = udim - 1 + udim * i;
        to[k++] = udim - 1 + udim * (i + 1);
    }
    if (uclosed)
        for (i = 0; i < vdim; i++) {
            from[k] = i * udim;
            to[k++] = i * udim + udim - 1;
        }
    if (vclosed)
        for (i = 0; i < udim; i++) {
            from[k] = i;
            to[k++] = i + udim * (vdim - 1);
        }
    return (cachedWire = new WireframeMesh(point, from, to));
}
