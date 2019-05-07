/**
     * Write session to file.
     */
private void writeSession() {
    boolean modified = session.isModified();
    try {
        waitDialog.show(LANGUAGE.getString("MainFrame.SavingFile") + " " + sessionFile);
        FileOutputStream fout = new FileOutputStream(sessionFile);
        GZIPOutputStream gout = new GZIPOutputStream(fout);
        BufferedOutputStream bout = new BufferedOutputStream(gout);
        XMLEncoder enc = new XMLEncoder(bout);
        session.setModified(false);
        enc.writeObject(session);
        enc.close();
        waitDialog.stop();
    } catch (IOException ex) {
        session.setModified(modified);
        waitDialog.stop();
        fileWriteError(sessionFile);
    }
}
