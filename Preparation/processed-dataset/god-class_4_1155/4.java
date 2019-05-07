/**
     * Change the windowed mode.
     * @param windowed Use <code>true</code> for windowed mode
     *      and <code>false</code> for fullscreen mode.
     */
public void changeWindowedMode(boolean windowed) {
    JMenuBar menuBar = null;
    if (frame != null) {
        menuBar = frame.getJMenuBar();
        if (frame instanceof WindowedFrame) {
            this.windowBounds = frame.getBounds();
        }
        frame.setVisible(false);
        frame.dispose();
    }
    setWindowed(windowed);
    this.frame = FreeColFrame.createFreeColFrame(freeColClient, canvas, gd, windowed);
    frame.setJMenuBar(menuBar);
    frame.setCanvas(canvas);
    frame.updateBounds(getWindowBounds());
    mapViewer.forceReposition();
    canvas.updateSizes();
    frame.setVisible(true);
}
