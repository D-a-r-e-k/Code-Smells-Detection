//}}}  
//{{{ _createInputStream() method  
/**
	 * Creates an input stream. This method is called from the I/O
	 * thread.
	 * @param session the VFS session
	 * @param path The path
	 * @param ignoreErrors If true, file not found errors should be
	 * ignored
	 * @param comp The component that will parent error dialog boxes
	 * @return an inputstream or <code>null</code> if there was a problem
	 * @exception IOException If an I/O error occurs
	 * @since jEdit 2.7pre1
	 */
public InputStream _createInputStream(Object session, String path, boolean ignoreErrors, Component comp) throws IOException {
    VFSManager.error(comp, path, "vfs.not-supported.load", new String[] { name });
    return null;
}
