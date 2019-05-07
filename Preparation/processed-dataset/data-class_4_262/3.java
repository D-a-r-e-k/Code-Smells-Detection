//}}}  
//{{{ insertFile() method  
/**
	 * Loads a file from disk, and inserts it into this buffer.
	 * @param view The view
	 * @param path the path of the file to insert
	 *
	 * @since 4.0pre1
	 */
public boolean insertFile(View view, String path) {
    if (isPerformingIO()) {
        GUIUtilities.error(view, "buffer-multiple-io", null);
        return false;
    }
    setBooleanProperty(BufferIORequest.ERROR_OCCURRED, false);
    path = MiscUtilities.constructPath(this.path, path);
    Buffer buffer = jEdit.getBuffer(path);
    if (buffer != null) {
        view.getTextArea().setSelectedText(buffer.getText(0, buffer.getLength()));
        return true;
    }
    VFS vfs = VFSManager.getVFSForPath(path);
    // this returns false if initial sanity  
    // checks (if the file is a directory, etc)  
    // fail  
    return vfs.insert(view, this, path);
}
