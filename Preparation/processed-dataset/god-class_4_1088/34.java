//}}}  
//{{{ searchInDirectory() method  
/**
	 * Opens a directory search in the specified directory.
	 * @param path The path name
	 * @param directory True if the path is a directory, false if it is a file
	 * @since jEdit 4.2pre1
	 */
public void searchInDirectory(String path, boolean directory) {
    String filter;
    VFSFileFilter vfsff = getVFSFileFilter();
    if (vfsff instanceof GlobVFSFileFilter)
        filter = ((GlobVFSFileFilter) vfsff).getGlob();
    else
        filter = "*";
    if (!directory) {
        String name = MiscUtilities.getFileName(path);
        String ext = MiscUtilities.getFileExtension(name);
        filter = (ext == null || ext.length() == 0 ? filter : '*' + ext);
        path = MiscUtilities.getParentOfPath(path);
    }
    SearchAndReplace.setSearchFileSet(new DirectoryListSet(path, filter, true));
    SearchDialog.showSearchDialog(view, null, SearchDialog.DIRECTORY);
}
