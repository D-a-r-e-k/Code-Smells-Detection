//}}}  
//{{{ browseDirectoryInNewWindow() method  
/**
	 * Opens the specified directory in a new, floating, file system browser.
	 * @param view The view
	 * @param path The directory's path
	 * @since jEdit 4.1pre2
	 */
public static void browseDirectoryInNewWindow(View view, String path) {
    DockableWindowManager wm = view.getDockableWindowManager();
    if (path != null) {
        // this is such a bad way of doing it, but oh well...  
        jEdit.setTemporaryProperty("vfs.browser.path.tmp", path);
    }
    wm.floatDockableWindow("vfs.browser");
    jEdit.unsetProperty("vfs.browser.path.tmp");
}
