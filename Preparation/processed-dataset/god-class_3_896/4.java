/**
     * Overridden so we can exit on system close.
     */
protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        exit();
    }
}
