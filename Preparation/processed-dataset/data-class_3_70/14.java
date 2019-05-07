/**
     * Opens the session from a file.
     */
private void openSession() {
    if (saveOldSession()) {
        fileChooser.setFileFilter(jmoneyFileFilter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File oldFile = sessionFile;
            setSessionFile(fileChooser.getSelectedFile());
            readSession();
            if (sessionFile == null)
                setSessionFile(oldFile);
        }
    }
}
