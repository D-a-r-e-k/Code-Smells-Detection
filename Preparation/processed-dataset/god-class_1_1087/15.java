//}}}  
//{{{ copy() methods  
/**
	 * Copy a file to another using VFS.
	 *
	 * @param progress the progress observer. It could be null if you don't want to monitor progress. If not null
	 *                  you should probably launch this command in a WorkThread
	 * @param sourceVFS the source VFS
	 * @param sourceSession the VFS session
	 * @param sourcePath the source path
	 * @param targetVFS the target VFS
	 * @param targetSession the target session
	 * @param targetPath the target path
	 * @param comp comp The component that will parent error dialog boxes
	 * @param canStop could this copy be stopped ?
	 * @return true if the copy was successful
	 * @throws IOException  IOException If an I/O error occurs
	 * @since jEdit 4.3pre3
	 */
public static boolean copy(ProgressObserver progress, VFS sourceVFS, Object sourceSession, String sourcePath, VFS targetVFS, Object targetSession, String targetPath, Component comp, boolean canStop) throws IOException {
    if (progress != null)
        progress.setStatus("Initializing");
    InputStream in = null;
    OutputStream out = null;
    try {
        VFSFile sourceVFSFile = sourceVFS._getFile(sourceSession, sourcePath, comp);
        if (sourceVFSFile == null)
            throw new FileNotFoundException(sourcePath);
        if (progress != null) {
            progress.setMaximum(sourceVFSFile.getLength());
        }
        VFSFile targetVFSFile = targetVFS._getFile(targetSession, targetPath, comp);
        if (targetVFSFile.getType() == VFSFile.DIRECTORY) {
            if (targetVFSFile.getPath().equals(sourceVFSFile.getPath()))
                return false;
            targetPath = MiscUtilities.constructPath(targetPath, sourceVFSFile.getName());
        }
        in = new BufferedInputStream(sourceVFS._createInputStream(sourceSession, sourcePath, false, comp));
        out = new BufferedOutputStream(targetVFS._createOutputStream(targetSession, targetPath, comp));
        boolean copyResult = IOUtilities.copyStream(IOBUFSIZE, progress, in, out, canStop);
        VFSManager.sendVFSUpdate(targetVFS, targetPath, true);
        return copyResult;
    } finally {
        IOUtilities.closeQuietly(in);
        IOUtilities.closeQuietly(out);
    }
}
