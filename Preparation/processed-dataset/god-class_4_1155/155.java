public void startMapEditorGUI() {
    // We may need to reset the zoom value to the default value 
    scaleMap(2f);
    setupMapEditorMenuBar();
    canvas.showMapEditorTransformPanel();
    setupMouseListenerForMapEditor();
}
