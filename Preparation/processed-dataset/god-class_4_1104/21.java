/** Set the size of the mesh. */
public void setSize(double xsize, double ysize, double zsize) {
    Vec3 size = getBounds().getSize();
    double xscale, yscale, zscale;
    if (size.x == 0.0)
        xscale = 1.0;
    else
        xscale = xsize / size.x;
    if (size.y == 0.0)
        yscale = 1.0;
    else
        yscale = ysize / size.y;
    if (size.z == 0.0)
        zscale = 1.0;
    else
        zscale = zsize / size.z;
    for (int i = 0; i < vertex.length; i++) {
        vertex[i].r.x *= xscale;
        vertex[i].r.y *= yscale;
        vertex[i].r.z *= zscale;
    }
    skeleton.scale(xscale, yscale, zscale);
    cachedMesh = null;
    cachedWire = null;
    bounds = null;
}
