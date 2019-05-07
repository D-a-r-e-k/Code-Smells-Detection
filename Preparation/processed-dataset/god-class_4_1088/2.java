//}}}  
//{{{ browseDirectory() method  
/**
	 * Opens the specified directory in a file system browser.
	 * @param view The view
	 * @param path The directory's path
	 * @since jEdit 4.0pre3
	 */
public static void browseDirectory(View view, String path) {
    DockableWindowManager wm = view.getDockableWindowManager();
    VFSBrowser browser = (VFSBrowser) wm.getDockable(NAME);
    if (browser != null) {
        wm.showDockableWindow(NAME);
        browser.setDirectory(path);
    } else {
        if (path != null) {
            // this is such a bad way of doing it, but oh well...  
            jEdit.setTemporaryProperty("vfs.browser.path.tmp", path);
        }
        wm.addDockableWindow("vfs.browser");
        jEdit.unsetProperty("vfs.browser.path.tmp");
    }
}
