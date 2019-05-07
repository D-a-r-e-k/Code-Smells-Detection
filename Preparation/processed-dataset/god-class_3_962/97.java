/**
   * Opens a file in the MRU list.
   */
protected void requestOpenMRU(ActionEvent e) {
    String file = e.getActionCommand();
    StringTokenizer st = new StringTokenizer(file);
    String num = st.nextToken().trim();
    file = st.nextToken("\n");
    try {
        int index = Integer.parseInt(num) - 1;
        InputStream in = _mruFileManager.getInputStream(index);
        LogFileParser lfp = new LogFileParser(in);
        lfp.parse(this);
        _mruFileManager.moveToTop(index);
        updateMRUList();
    } catch (Exception me) {
        LogFactor5ErrorDialog error = new LogFactor5ErrorDialog(getBaseFrame(), "Unable to load file " + file);
    }
}
