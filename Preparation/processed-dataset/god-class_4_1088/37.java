//}}}  
//{{{ locateFile() method  
/**
	 * Goes to the given file's directory and selects the file in the list.
	 * @param path The file
	 * @since jEdit 4.2pre2
	 */
public void locateFile(final String path) {
    VFSFileFilter filter = getVFSFileFilter();
    if (!filter.accept(MiscUtilities.getFileName(path)))
        setFilenameFilter(null);
    setDirectory(MiscUtilities.getParentOfPath(path));
    VFSManager.runInAWTThread(new Runnable() {

        public void run() {
            browserView.getTable().selectFile(path);
        }
    });
}
