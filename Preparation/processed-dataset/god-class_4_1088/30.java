//}}}		  
//{{{ mkdir() method  
public void mkdir() {
    String newDirectory = GUIUtilities.input(this, "vfs.browser.mkdir", null);
    if (newDirectory == null)
        return;
    // if a directory is selected, create new dir in there.  
    // if a file is selected, create new dir inside its parent.  
    final VFSFile[] selected = getSelectedFiles();
    String parent;
    if (selected.length == 0)
        parent = path;
    else if (selected[0].getType() == VFSFile.FILE) {
        parent = selected[0].getPath();
        parent = VFSManager.getVFSForPath(parent).getParentOfPath(parent);
    } else
        parent = selected[0].getPath();
    VFS vfs = VFSManager.getVFSForPath(parent);
    // path is the currently viewed directory in the browser  
    newDirectory = MiscUtilities.constructPath(parent, newDirectory);
    Object session = vfs.createVFSSession(newDirectory, this);
    if (session == null)
        return;
    if (!startRequest())
        return;
    VFSManager.runInWorkThread(new BrowserIORequest(BrowserIORequest.MKDIR, this, session, vfs, newDirectory, null, null));
    VFSManager.runInAWTThread(new Runnable() {

        public void run() {
            endRequest();
            if (selected.length != 0 && selected[0].getType() != VFSFile.FILE) {
                VFSDirectoryEntryTable directoryEntryTable = browserView.getTable();
                int selectedRow = directoryEntryTable.getSelectedRow();
                VFSDirectoryEntryTableModel model = (VFSDirectoryEntryTableModel) directoryEntryTable.getModel();
                VFSDirectoryEntryTableModel.Entry entry = model.files[selectedRow];
                if (!entry.expanded) {
                    browserView.clearExpansionState();
                    browserView.loadDirectory(entry, entry.dirEntry.getPath(), false);
                }
            }
        }
    });
}
