public Dimension determineWindowSize() {
    Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    Dimension size = new Dimension(bounds.width - DEFAULT_WINDOW_SPACE, bounds.height - DEFAULT_WINDOW_SPACE);
    return size;
}
