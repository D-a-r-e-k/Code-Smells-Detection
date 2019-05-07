/**
	 * Copy a file to another using VFS.
	 *
	 * @param progress the progress observer. It could be null if you don't want to monitor progress. If not null
	 *                  you should probably launch this command in a WorkThread
	 * @param sourcePath the source path
	 * @param targetPath the target path
	 * @param comp comp The component that will parent error dialog boxes
	 * @param canStop if true the copy can be stopped
	 * @return true if the copy was successful
	 * @throws IOException IOException If an I/O error occurs
	 * @since jEdit 4.3pre3
	 */
public static boolean copy(ProgressObserver progress, String sourcePath, String targetPath, Component comp, boolean canStop) throws IOException {
    VFS sourceVFS = VFSManager.getVFSForPath(sourcePath);
    Object sourceSession = sourceVFS.createVFSSession(sourcePath, comp);
    if (sourceSession == null) {
        Log.log(Log.WARNING, VFS.class, "Unable to get a valid session from " + sourceVFS + " for path " + sourcePath);
        return false;
    }
    VFS targetVFS = VFSManager.getVFSForPath(targetPath);
    Object targetSession = targetVFS.createVFSSession(targetPath, comp);
    if (targetSession == null) {
        Log.log(Log.WARNING, VFS.class, "Unable to get a valid session from " + targetVFS + " for path " + targetPath);
        return false;
    }
    return copy(progress, sourceVFS, sourceSession, sourcePath, targetVFS, targetSession, targetPath, comp, canStop);
}
