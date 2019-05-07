//}}}  
//{{{ _listDirectory() method  
/**
	 * A convenience method that filters the directory listing
	 * according to a filter, and can optionally list the directory
	 * recursively.
	 * @param session The session
	 * @param directory The directory. Note that this must be a full
	 * URL, including the host name, path name, and so on. The
	 * username and password (if needed by the VFS) is obtained from the
	 * session instance.
	 * @param filter The {@link VFSFileFilter} to use for filtering.
	 * @param recursive If true, subdirectories will also be listed.
	 * @param comp The component that will parent error dialog boxes
	 * @exception IOException if an I/O error occurred
	 * @param skipBinary ignore binary files (do not return them).
	 *    This will slow down the process since it will open the files
	 * @param skipHidden skips hidden files, directories, and
	 *        backup files. Ignores any file beginning with . or #, or ending with ~
	 *        or .bak
	 *
	 * @since jEdit 4.3pre7
	 */
public String[] _listDirectory(Object session, String directory, VFSFileFilter filter, boolean recursive, Component comp, boolean skipBinary, boolean skipHidden) throws IOException {
    List<String> files = new ArrayList<String>(100);
    listFiles(session, new HashSet<String>(), files, directory, filter, recursive, comp, skipBinary, skipHidden);
    String[] retVal = files.toArray(new String[files.size()]);
    Arrays.sort(retVal, new StandardUtilities.StringCompare<String>(true));
    return retVal;
}
