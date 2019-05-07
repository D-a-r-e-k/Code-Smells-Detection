/**
   * Loads and parses a log file.
   */
protected boolean loadLogFile(File file) {
    boolean ok = false;
    try {
        LogFileParser lfp = new LogFileParser(file);
        lfp.parse(this);
        ok = true;
    } catch (IOException e) {
        LogFactor5ErrorDialog error = new LogFactor5ErrorDialog(getBaseFrame(), "Error reading " + file.getName());
    }
    return ok;
}
