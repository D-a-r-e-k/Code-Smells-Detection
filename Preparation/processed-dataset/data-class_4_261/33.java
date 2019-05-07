//}}} 		  
//{{{ searchInDirectory() method  
/**
	 * Opens a directory search in the current directory.
	 * @since jEdit 4.0pre2
	 */
public void searchInDirectory() {
    VFSFile[] selected = getSelectedFiles();
    if (selected.length >= 1) {
        VFSFile file = selected[0];
        searchInDirectory(file.getPath(), file.getType() != VFSFile.FILE);
    } else {
        searchInDirectory(path, true);
    }
}
