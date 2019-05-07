public Object3D duplicate() {
    SplineMesh mesh = new SplineMesh();
    mesh.copyObject(this);
    return mesh;
}
