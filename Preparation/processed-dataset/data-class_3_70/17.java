/**
     * Saves the session in a user specified file.
     */
private void saveSessionAs() {
    fileChooser.setFileFilter(jmoneyFileFilter);
    int result = fileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        if (dontOverwrite(file))
            return;
        setSessionFile(file);
        writeSession();
    }
}
