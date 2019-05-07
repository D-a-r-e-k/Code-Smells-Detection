public void edit(EditingWindow parent, ObjectInfo info, Runnable cb) {
    SplineMeshEditorWindow ed = new SplineMeshEditorWindow(parent, "Spline Mesh '" + info.getName() + "'", info, cb, true);
    ed.setVisible(true);
}
