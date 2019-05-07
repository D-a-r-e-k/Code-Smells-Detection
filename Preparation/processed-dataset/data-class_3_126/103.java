/**
   * Loads a parses a log file running on a server.
   */
protected boolean loadLogFile(URL url) {
    boolean ok = false;
    try {
        LogFileParser lfp = new LogFileParser(url.openStream());
        lfp.parse(this);
        ok = true;
    } catch (IOException e) {
        LogFactor5ErrorDialog error = new LogFactor5ErrorDialog(getBaseFrame(), "Error reading URL:" + url.getFile());
    }
    return ok;
}
