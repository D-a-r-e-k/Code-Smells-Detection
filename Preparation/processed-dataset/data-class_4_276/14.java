/** Set the smoothing method. */
public void setSmoothingMethod(int method) {
    smoothingMethod = method;
    cachedMesh = null;
    cachedWire = null;
    bounds = null;
}
