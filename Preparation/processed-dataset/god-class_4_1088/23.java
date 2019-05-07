// }}}	  
//{{{ setDirectory() method  
public void setDirectory(String path) {
    if (path.startsWith("file:"))
        path = path.substring(5);
    path = MiscUtilities.expandVariables(path);
    pathField.setText(path);
    if (!startRequest())
        return;
    historyStack.push(path);
    browserView.saveExpansionState();
    browserView.loadDirectory(null, path, true);
    this.path = path;
    VFSManager.runInAWTThread(new Runnable() {

        public void run() {
            endRequest();
        }
    });
}
