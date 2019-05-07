//}}}  
//{{{ rename() method  
public void rename(String from, String newname) {
    VFS vfs = VFSManager.getVFSForPath(from);
    String filename = vfs.getFileName(from);
    String[] args = { filename };
    String to = newname;
    if (to == null || filename.equals(newname))
        return;
    to = MiscUtilities.constructPath(vfs.getParentOfPath(from), to);
    Object session = vfs.createVFSSession(from, this);
    if (session == null)
        return;
    if (!startRequest())
        return;
    VFSManager.runInWorkThread(new BrowserIORequest(BrowserIORequest.RENAME, this, session, vfs, from, to, null));
    VFSManager.runInAWTThread(new Runnable() {

        public void run() {
            endRequest();
        }
    });
}
