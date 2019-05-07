/** Set the position, smoothness, and thickness values for all points. */
public void setShape(MeshVertex v[], float smoothness[], double thickness[]) {
    vertex = v;
    this.thickness = thickness;
    this.smoothness = smoothness;
    clearCachedMesh();
}
