//}}}  
// A method name that starts with _ requires a session object  
//{{{ _canonPath() method  
/**
	 * Returns the canonical form of the specified path name. For example,
	 * <code>~</code> might be expanded to the user's home directory.
	 * @param session The session
	 * @param path The path
	 * @param comp The component that will parent error dialog boxes
	 * @exception IOException if an I/O error occurred
	 * @since jEdit 4.0pre2
	 */
public String _canonPath(Object session, String path, Component comp) throws IOException {
    return path;
}
