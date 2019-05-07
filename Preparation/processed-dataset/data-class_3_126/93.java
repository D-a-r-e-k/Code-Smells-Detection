/**
   * Uses a JFileChooser to select a file to opened with the
   * LF5 GUI.
   */
protected void requestOpen() {
    JFileChooser chooser;
    if (_fileLocation == null) {
        chooser = new JFileChooser();
    } else {
        chooser = new JFileChooser(_fileLocation);
    }
    int returnVal = chooser.showOpenDialog(_logMonitorFrame);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File f = chooser.getSelectedFile();
        if (loadLogFile(f)) {
            _fileLocation = chooser.getSelectedFile();
            _mruFileManager.set(f);
            updateMRUList();
        }
    }
}
