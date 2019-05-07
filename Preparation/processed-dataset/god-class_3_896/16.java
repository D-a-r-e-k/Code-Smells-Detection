/**
     * Saves the session in the selected file.
     */
private void saveSession() {
    if (sessionFile == null)
        saveSessionAs();
    else
        writeSession();
}
