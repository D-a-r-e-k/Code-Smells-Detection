/**
     * Read session from file.
     */
private void readSession() {
    try {
        waitDialog.show(LANGUAGE.getString("MainFrame.OpeningFile") + " " + sessionFile);
        FileInputStream fin = new FileInputStream(sessionFile);
        GZIPInputStream gin = new GZIPInputStream(fin);
        BufferedInputStream bin = new BufferedInputStream(gin);
        XMLDecoder dec = new XMLDecoder(bin);
        Session newSession = (Session) dec.readObject();
        dec.close();
        setSession(newSession);
        waitDialog.stop();
    } catch (IOException ex) {
        waitDialog.stop();
        fileReadError(sessionFile);
        setSessionFile(null);
    }
}
