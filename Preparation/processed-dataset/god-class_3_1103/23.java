public void edit(EditingWindow parent, ObjectInfo info, Runnable cb) {
    TubeEditorWindow ed = new TubeEditorWindow(parent, "Tube object '" + info.getName() + "'", info, cb, true);
    ed.setVisible(true);
}
