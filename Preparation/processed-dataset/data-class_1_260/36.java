//}}}  
//{{{ recursive listFiles() method  
private void listFiles(Object session, Collection<String> stack, List<String> files, String directory, VFSFileFilter filter, boolean recursive, Component comp, boolean skipBinary, boolean skipHidden) throws IOException {
    String resolvedPath = directory;
    if (recursive && !MiscUtilities.isURL(directory)) {
        resolvedPath = MiscUtilities.resolveSymlinks(directory);
        /*
			 * If looking at a symlink, do not traverse the
			 * resolved path more than once.
			 */
        if (!directory.equals(resolvedPath)) {
            if (stack.contains(resolvedPath)) {
                Log.log(Log.ERROR, this, "Recursion in listFiles(): " + directory);
                return;
            }
            stack.add(resolvedPath);
        }
    }
    Thread ct = Thread.currentThread();
    WorkThread wt = null;
    if (ct instanceof WorkThread) {
        wt = (WorkThread) ct;
    }
    VFSFile[] _files = _listFiles(session, directory, comp);
    if (_files == null || _files.length == 0)
        return;
    for (int i = 0; i < _files.length; i++) {
        if (wt != null && wt.isAborted())
            break;
        VFSFile file = _files[i];
        if (skipHidden && (file.isHidden() || MiscUtilities.isBackup(file.getName())))
            continue;
        if (!filter.accept(file))
            continue;
        if (file.getType() == VFSFile.DIRECTORY || file.getType() == VFSFile.FILESYSTEM) {
            if (recursive) {
                String canonPath = _canonPath(session, file.getPath(), comp);
                listFiles(session, stack, files, canonPath, filter, recursive, comp, skipBinary, skipHidden);
            }
        } else // It's a regular file  
        {
            if (skipBinary) {
                try {
                    if (file.isBinary(session)) {
                        Log.log(Log.NOTICE, this, file.getPath() + ": skipped as a binary file");
                        continue;
                    }
                } catch (IOException e) {
                    Log.log(Log.ERROR, this, e);
                }
            }
            files.add(file.getPath());
        }
    }
}
