/** Calculate the (approximate) bounding box for the mesh. */
void findBounds() {
    double minx, miny, minz, maxx, maxy, maxz;
    Vec3 vert[];
    int i;
    if (cachedMesh != null)
        vert = cachedMesh.vert;
    else if (cachedWire != null)
        vert = cachedWire.vert;
    else {
        getWireframeMesh();
        vert = cachedWire.vert;
    }
    minx = maxx = vert[0].x;
    miny = maxy = vert[0].y;
    minz = maxz = vert[0].z;
    for (i = 1; i < vert.length; i++) {
        if (vert[i].x < minx)
            minx = vert[i].x;
        if (vert[i].x > maxx)
            maxx = vert[i].x;
        if (vert[i].y < miny)
            miny = vert[i].y;
        if (vert[i].y > maxy)
            maxy = vert[i].y;
        if (vert[i].z < minz)
            minz = vert[i].z;
        if (vert[i].z > maxz)
            maxz = vert[i].z;
    }
    bounds = new BoundingBox(minx, maxx, miny, maxy, minz, maxz);
}
