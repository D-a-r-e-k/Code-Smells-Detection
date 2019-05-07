//}}}  
//{{{ _listFiles() method  
/**
	 * Lists the specified directory.
	 * @param session The session
	 * @param directory The directory. Note that this must be a full
	 * URL, including the host name, path name, and so on. The
	 * username and password (if needed by the VFS) is obtained from the
	 * session instance.
	 * @param comp The component that will parent error dialog boxes
	 * @exception IOException if an I/O error occurred
	 * @since jEdit 4.3pre2
	 */
public VFSFile[] _listFiles(Object session, String directory, Component comp) throws IOException {
    return _listDirectory(session, directory, comp);
}
