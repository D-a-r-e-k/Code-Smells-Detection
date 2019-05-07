public void resetMenuBar() {
    JMenuBar menuBar = frame.getJMenuBar();
    if (menuBar != null) {
        ((FreeColMenuBar) menuBar).reset();
    }
}
