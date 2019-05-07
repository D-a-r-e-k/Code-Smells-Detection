//}}}  
//{{{ _createOutputStream() method  
/**
	 * Creates an output stream. This method is called from the I/O
	 * thread.
	 * @param session the VFS session
	 * @param path The path
	 * @param comp The component that will parent error dialog boxes
	 * @exception IOException If an I/O error occurs
	 * @since jEdit 2.7pre1
	 */
public OutputStream _createOutputStream(Object session, String path, Component comp) throws IOException {
    VFSManager.error(comp, path, "vfs.not-supported.save", new String[] { name });
    return null;
}
