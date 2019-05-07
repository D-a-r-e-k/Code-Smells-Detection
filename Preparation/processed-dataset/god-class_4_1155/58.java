public void setUpMouseListenersForCanvas() {
    canvas.addMouseListener(new CanvasMouseListener(freeColClient, canvas, mapViewer));
    canvas.addMouseMotionListener(new CanvasMouseMotionListener(freeColClient, mapViewer));
}
