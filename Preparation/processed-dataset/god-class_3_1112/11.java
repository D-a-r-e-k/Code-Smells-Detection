private void rawLeadinPrompt() {
    if (!interactive) {
        return;
    }
    if (rawLeadinPrompt == null) {
        throw new RuntimeException("Internal assertion failed.  " + "Scanner's message Resource Bundle not initialized properly");
    }
    psStd.println(rawLeadinPrompt);
}
