/** Returns the current master working directory, or the user's current directory if none is set. 20040213 Changed default 
   *  value to user's current directory.
   */
private File _getWorkDir() {
    File workDir = _mainFrame.getModel().getMasterWorkingDirectory();
    // cannot be null 
    assert workDir != null;
    if (workDir.isDirectory())
        return workDir;
    if (workDir.getParent() != null)
        workDir = workDir.getParentFile();
    return workDir;
}
