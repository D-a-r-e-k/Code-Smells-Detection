//}}}  
//{{{ showBrowseDialog() method  
/**
	 * Displays a dialog box that should set up a session and return
	 * the initial URL to browse.
	 * @param session Where the VFS session will be stored
	 * @param comp The component that will parent error dialog boxes
	 * @return The URL
	 * @since jEdit 2.7pre1
	 * @deprecated This function is not used in the jEdit core anymore,
	 *             so it doesn't have to be provided anymore. If you want
	 *             to use it for another purpose like in the FTP plugin,
	 *             feel free to do so.
	 */
@Deprecated
public String showBrowseDialog(Object[] session, Component comp) {
    return null;
}
