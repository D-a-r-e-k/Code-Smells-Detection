//}}}  
//{{{ setPath() method  
private void setPath(final String path) {
    jEdit.visit(new JEditVisitorAdapter() {

        @Override
        public void visit(EditPane editPane) {
            editPane.bufferRenamed(Buffer.this.path, path);
        }
    });
    this.path = path;
    VFS vfs = VFSManager.getVFSForPath(path);
    if ((vfs.getCapabilities() & VFS.WRITE_CAP) == 0)
        setFileReadOnly(true);
    name = vfs.getFileName(path);
    directory = vfs.getParentOfPath(path);
    if (vfs instanceof FileVFS) {
        file = new File(path);
        symlinkPath = MiscUtilities.resolveSymlinks(path);
        // if we don't do this, the autosave file won't be  
        // deleted after a save as  
        if (autosaveFile != null)
            autosaveFile.delete();
        autosaveFile = new File(file.getParent(), '#' + name + '#');
    } else {
        // I wonder if the lack of this broke anything in the  
        // past?  
        file = null;
        autosaveFile = null;
        symlinkPath = path;
    }
}
