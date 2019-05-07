/**
     * Exit application.
     */
private void exit() {
    if (saveOldSession()) {
        storeProperties();
        System.exit(0);
    }
}
