/** Get a MeshViewer which can be used for viewing this mesh. */
public MeshViewer createMeshViewer(MeshEditController controller, RowContainer options) {
    return new SplineMeshViewer(controller, options);
}
