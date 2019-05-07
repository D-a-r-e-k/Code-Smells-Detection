//}}}  
//{{{ saveAs() method  
/**
	 * Prompts the user for a file to save this buffer to.
	 * @param view The view
	 * @param rename True if the buffer's path should be changed, false
	 * if only a copy should be saved to the specified filename
	 * @since jEdit 2.6pre5
	 */
public boolean saveAs(View view, boolean rename) {
    String[] files = GUIUtilities.showVFSFileDialog(view, path, VFSBrowser.SAVE_DIALOG, false);
    // files[] should have length 1, since the dialog type is  
    // SAVE_DIALOG  
    if (files == null)
        return false;
    return save(view, files[0], rename);
}
