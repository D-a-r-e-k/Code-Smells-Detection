/** Set the thickness of the tube at each vertex. */
public void setThickness(double thickness[]) {
    this.thickness = thickness;
    clearCachedMesh();
}
