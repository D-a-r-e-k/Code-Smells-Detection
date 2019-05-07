//}}}  
//{{{ _listDirectory() method  
/**
	 * A convinience method that matches file names against globs, and can
	 * optionally list the directory recursively.
	 * @param session The session
	 * @param directory The directory. Note that this must be a full
	 * URL, including the host name, path name, and so on. The
	 * username and password (if needed by the VFS) is obtained from the
	 * session instance.
	 * @param glob Only file names matching this glob will be returned
	 * @param recursive If true, subdirectories will also be listed.
	 * @param comp The component that will parent error dialog boxes
	 * @exception IOException if an I/O error occurred
	 * @since jEdit 4.1pre1
	 */
public String[] _listDirectory(Object session, String directory, String glob, boolean recursive, Component comp) throws IOException {
    String[] retval = _listDirectory(session, directory, glob, recursive, comp, true, false);
    return retval;
}
