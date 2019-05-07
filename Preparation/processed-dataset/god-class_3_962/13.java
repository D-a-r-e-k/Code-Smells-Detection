public void setFrameSize(int width, int height) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    if (0 < width && width < screen.width) {
        _logMonitorFrameWidth = width;
    }
    if (0 < height && height < screen.height) {
        _logMonitorFrameHeight = height;
    }
    updateFrameSize();
}
