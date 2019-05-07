public void editGesture(final EditingWindow parent, ObjectInfo info, Runnable cb, ObjectInfo realObject) {
    SplineMeshEditorWindow ed = new SplineMeshEditorWindow(parent, "Gesture '" + info.getName() + "'", info, cb, false);
    ViewerCanvas views[] = ed.getAllViews();
    for (int i = 0; i < views.length; i++) ((MeshViewer) views[i]).setScene(parent.getScene(), realObject);
    ed.setVisible(true);
}
