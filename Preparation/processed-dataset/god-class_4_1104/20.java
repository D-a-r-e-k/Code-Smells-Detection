/** Set whether this mesh is closed in each direction. */
public void setClosed(boolean u, boolean v) {
    uclosed = u;
    vclosed = v;
    cachedMesh = null;
    cachedWire = null;
    bounds = null;
}
