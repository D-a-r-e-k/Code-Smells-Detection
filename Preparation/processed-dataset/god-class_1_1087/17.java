//}}}  
//{{{ insert() method  
/**
	 * Inserts a file into the specified buffer. The default implementation
	 * posts an I/O request to the I/O thread.
	 * @param view The view
	 * @param buffer The buffer
	 * @param path The path
	 */
public boolean insert(View view, Buffer buffer, String path) {
    if ((getCapabilities() & READ_CAP) == 0) {
        VFSManager.error(view, path, "vfs.not-supported.load", new String[] { name });
        return false;
    }
    Object session = createVFSSession(path, view);
    if (session == null)
        return false;
    VFSManager.runInWorkThread(new BufferInsertRequest(view, buffer, session, this, path));
    return true;
}
