//}}}  
//{{{ _saveComplete() method  
/**
	 * Called after a file has been saved.
	 * @param session The VFS session
	 * @param buffer The buffer
	 * @param path The path the buffer was saved to (can be different from
	 * {@link org.gjt.sp.jedit.Buffer#getPath()} if the user invoked the
	 * <b>Save a Copy As</b> command, for example).
	 * @param comp The component that will parent error dialog boxes
	 * @exception IOException If an I/O error occurs
	 * @since jEdit 4.1pre9
	 */
public void _saveComplete(Object session, Buffer buffer, String path, Component comp) throws IOException {
}
