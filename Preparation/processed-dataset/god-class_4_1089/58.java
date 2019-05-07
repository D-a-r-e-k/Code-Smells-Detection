//}}}  
//{{{ updateMarkersFile() method  
/**
	 * Save the markers file, or delete it when there are mo markers left
	 * Handling markers is now independent from saving the buffer.
	 * Changing markers will not set the buffer dirty any longer.
	 * @param view The current view
	 * @since jEdit 4.3pre7
	 */
public boolean updateMarkersFile(View view) {
    if (!markersChanged())
        return true;
    // adapted from VFS.save  
    VFS vfs = VFSManager.getVFSForPath(getPath());
    if (((vfs.getCapabilities() & VFS.WRITE_CAP) == 0) || !vfs.isMarkersFileSupported()) {
        VFSManager.error(view, path, "vfs.not-supported.save", new String[] { "markers file" });
        return false;
    }
    Object session = vfs.createVFSSession(path, view);
    if (session == null)
        return false;
    VFSManager.runInWorkThread(new MarkersSaveRequest(view, this, session, vfs, path));
    return true;
}
