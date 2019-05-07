//}}}  
//{{{ delete() method  
/**
	 * Note that all files must be on the same VFS.
	 * @since jEdit 4.3pre2
	 */
public void delete(VFSFile[] files) {
    String dialogType;
    if (MiscUtilities.isURL(files[0].getDeletePath()) && FavoritesVFS.PROTOCOL.equals(MiscUtilities.getProtocolOfURL(files[0].getDeletePath()))) {
        dialogType = "vfs.browser.delete-favorites";
    } else {
        dialogType = "vfs.browser.delete-confirm";
    }
    StringBuilder buf = new StringBuilder();
    String typeStr = "files";
    for (int i = 0; i < files.length; i++) {
        buf.append(files[i].getPath());
        buf.append('\n');
        if (files[i].getType() == VFSFile.DIRECTORY)
            typeStr = "directories and their contents";
    }
    Object[] args = { buf.toString(), typeStr };
    int result = GUIUtilities.confirm(this, dialogType, args, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (result != JOptionPane.YES_OPTION)
        return;
    VFS vfs = VFSManager.getVFSForPath(files[0].getDeletePath());
    if (!startRequest())
        return;
    for (int i = 0; i < files.length; i++) {
        Object session = vfs.createVFSSession(files[i].getDeletePath(), this);
        if (session == null)
            continue;
        VFSManager.runInWorkThread(new BrowserIORequest(BrowserIORequest.DELETE, this, session, vfs, files[i].getDeletePath(), null, null));
    }
    VFSManager.runInAWTThread(new Runnable() {

        public void run() {
            endRequest();
        }
    });
}
