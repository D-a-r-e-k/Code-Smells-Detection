//}}}  
//{{{ reloadDirectory() method  
public void reloadDirectory() {
    // used by FTP plugin to clear directory cache  
    VFSManager.getVFSForPath(path).reloadDirectory(path);
    browserView.saveExpansionState();
    browserView.loadDirectory(null, path, false);
}
