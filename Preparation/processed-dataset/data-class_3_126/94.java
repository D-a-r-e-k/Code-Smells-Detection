/**
   * Uses a Dialog box to accept a URL to a file to be opened
   * with the LF5 GUI.
   */
protected void requestOpenURL() {
    LogFactor5InputDialog inputDialog = new LogFactor5InputDialog(getBaseFrame(), "Open URL", "URL:");
    String temp = inputDialog.getText();
    if (temp != null) {
        if (temp.indexOf("://") == -1) {
            temp = "http://" + temp;
        }
        try {
            URL url = new URL(temp);
            if (loadLogFile(url)) {
                _mruFileManager.set(url);
                updateMRUList();
            }
        } catch (MalformedURLException e) {
            LogFactor5ErrorDialog error = new LogFactor5ErrorDialog(getBaseFrame(), "Error reading URL.");
        }
    }
}
