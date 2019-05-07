//}}}  
//{{{ _finishTwoStageSave() method  
/**
	 * Called after a file has been saved and we use twoStageSave (first saving to
	 * another file). This should re-apply permissions for example.

	 * @param session The VFS session
	 * @param buffer The buffer
	 * @param path The path the buffer was saved to (can be different from
	 * {@link org.gjt.sp.jedit.Buffer#getPath()} if the user invoked the
	 * <b>Save a Copy As</b> command, for example).
	 * @param comp The component that will parent error dialog boxes
	 * @exception IOException If an I/O error occurs
	 * @since jEdit 4.3pre4
	 */
public void _finishTwoStageSave(Object session, Buffer buffer, String path, Component comp) throws IOException {
}
