public void setupInGameMenuBar() {
    frame.setJMenuBar(new InGameMenuBar(freeColClient, this));
    frame.paintAll(canvas.getGraphics());
}
