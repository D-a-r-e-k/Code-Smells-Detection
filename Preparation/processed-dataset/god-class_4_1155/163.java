private void setupMouseListenerForMapEditor() {
    CanvasMapEditorMouseListener listener = new CanvasMapEditorMouseListener(freeColClient, this, canvas);
    canvas.addMouseListener(listener);
    canvas.addMouseMotionListener(listener);
}
