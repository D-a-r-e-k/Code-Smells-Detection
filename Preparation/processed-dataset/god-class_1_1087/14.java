//}}}  
//{{{ save() method  
/**
	 * Saves the specifies buffer. The default implementation posts
	 * an I/O request to the I/O thread.
	 * @param view The view
	 * @param buffer The buffer
	 * @param path The path
	 */
public boolean save(View view, Buffer buffer, String path) {
    if ((getCapabilities() & WRITE_CAP) == 0) {
        VFSManager.error(view, path, "vfs.not-supported.save", new String[] { name });
        return false;
    }
    Object session = createVFSSession(path, view);
    if (session == null)
        return false;
    /* When doing a 'save as', the path to save to (path)
		 * will not be the same as the buffer's previous path
		 * (buffer.getPath()). In that case, we want to create
		 * a backup of the new path, even if the old path was
		 * backed up as well (BACKED_UP property set) */
    if (!path.equals(buffer.getPath()))
        buffer.unsetProperty(Buffer.BACKED_UP);
    VFSManager.runInWorkThread(new BufferSaveRequest(view, buffer, session, this, path));
    return true;
}
