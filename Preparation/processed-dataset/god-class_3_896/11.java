/**
     * Creates a new session. Used by Menu File->New.
     */
private void newSession() {
    if (saveOldSession()) {
        setSessionFile(null);
        setSession(new Session(0));
    }
}
