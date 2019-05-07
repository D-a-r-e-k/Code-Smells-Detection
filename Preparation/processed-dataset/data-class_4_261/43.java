//{{{ directoryLoaded() method  
void directoryLoaded(Object node, Object[] loadInfo, boolean addToHistory) {
    VFSManager.runInAWTThread(new DirectoryLoadedAWTRequest(node, loadInfo, addToHistory));
}
