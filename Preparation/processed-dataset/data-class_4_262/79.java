//}}}  
//{{{ checkFileForSave() method  
private static boolean checkFileForSave(View view, VFS vfs, String path) {
    if ((vfs.getCapabilities() & VFS.LOW_LATENCY_CAP) != 0) {
        Object session = vfs.createVFSSession(path, view);
        if (session == null)
            return false;
        try {
            VFSFile file = vfs._getFile(session, path, view);
            if (file == null)
                return true;
            if (file.getType() != VFSFile.FILE) {
                VFSManager.error(view, path, "ioerror.save-directory", null);
                return false;
            }
        } catch (IOException io) {
            VFSManager.error(view, path, "ioerror", new String[] { io.toString() });
            return false;
        } finally {
            try {
                vfs._endVFSSession(session, view);
            } catch (IOException io) {
                VFSManager.error(view, path, "ioerror", new String[] { io.toString() });
                return false;
            }
        }
    }
    return true;
}
