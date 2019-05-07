//}}}  
//{{{ checkFileForLoad() method  
private boolean checkFileForLoad(View view, VFS vfs, String path) {
    if ((vfs.getCapabilities() & VFS.LOW_LATENCY_CAP) != 0) {
        Object session = vfs.createVFSSession(path, view);
        if (session == null)
            return false;
        try {
            VFSFile file = vfs._getFile(session, path, view);
            if (file == null) {
                setNewFile(true);
                return true;
            }
            if (!file.isReadable()) {
                VFSManager.error(view, path, "ioerror.no-read", null);
                setNewFile(false);
                return false;
            }
            setFileReadOnly(!file.isWriteable());
            if (file.getType() != VFSFile.FILE) {
                VFSManager.error(view, path, "ioerror.open-directory", null);
                setNewFile(false);
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
