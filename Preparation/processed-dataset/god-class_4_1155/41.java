public void quit() throws Exception {
    if (!isWindowed()) {
        gd.setFullScreenWindow(null);
    }
}
