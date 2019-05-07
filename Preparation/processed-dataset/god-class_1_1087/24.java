//}}}  
//{{{ _getFile() method  
/**
	 * Returns the specified directory entry.
	 * @param session The session get it with {@link VFS#createVFSSession(String, Component)}
	 * @param path The path
	 * @param comp The component that will parent error dialog boxes
	 * @exception IOException if an I/O error occurred
	 * @return The specified directory entry, or null if it doesn't exist.
	 * @since jEdit 4.3pre2
	 */
public VFSFile _getFile(Object session, String path, Component comp) throws IOException {
    return _getDirectoryEntry(session, path, comp);
}
