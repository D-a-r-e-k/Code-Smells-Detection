//}}}  
//{{{ load() method  
/**
	 * Loads the specified buffer. The default implementation posts
	 * an I/O request to the I/O thread.
	 * @param view The view
	 * @param buffer The buffer
	 * @param path The path
	 */
public boolean load(View view, Buffer buffer, String path) {
    if ((getCapabilities() & READ_CAP) == 0) {
        VFSManager.error(view, path, "vfs.not-supported.load", new String[] { name });
        return false;
    }
    Object session = createVFSSession(path, view);
    if (session == null)
        return false;
    if ((getCapabilities() & WRITE_CAP) == 0)
        buffer.setReadOnly(true);
    BufferIORequest request = new BufferLoadRequest(view, buffer, session, this, path);
    if (buffer.isTemporary())
        // this makes HyperSearch much faster  
        request.run();
    else
        VFSManager.runInWorkThread(request);
    return true;
}
