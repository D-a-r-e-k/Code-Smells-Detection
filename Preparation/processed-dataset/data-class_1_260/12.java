//}}}  
//{{{ createVFSSession() method  
/**
	 * Creates a VFS session. This method is called from the AWT thread,
	 * so it should not do any I/O. It could, however, prompt for
	 * a login name and password, for example.
	 * @param path The path in question
	 * @param comp The component that will parent any dialog boxes shown
	 * @return The session. The session can be null if there were errors
	 * @since jEdit 2.6pre3
	 */
public Object createVFSSession(String path, Component comp) {
    return new Object();
}
