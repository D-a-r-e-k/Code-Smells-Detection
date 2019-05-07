/** Set the smoothness values. */
public void setSmoothness(float usmoothness[], float vsmoothness[]) {
    this.usmoothness = usmoothness;
    this.vsmoothness = vsmoothness;
    cachedMesh = null;
    cachedWire = null;
    bounds = null;
}
