//}}}  
//{{{ save() method  
/**
	 * Saves this buffer to the specified path name, or the current path
	 * name if it's null.
	 * @param view The view
	 * @param path The path name to save the buffer to, or null to use
	 * the existing path
	 * @param rename True if the buffer's path should be changed, false
	 * if only a copy should be saved to the specified filename
	 * @param disableFileStatusCheck  Disables file status checking
	 * regardless of the state of the checkFileStatus property
	 */
public boolean save(final View view, String path, final boolean rename, boolean disableFileStatusCheck) {
    if (isPerformingIO()) {
        GUIUtilities.error(view, "buffer-multiple-io", null);
        return false;
    }
    setBooleanProperty(BufferIORequest.ERROR_OCCURRED, false);
    if (path == null && getFlag(NEW_FILE))
        return saveAs(view, rename);
    if (path == null && file != null) {
        long newModTime = file.lastModified();
        if (newModTime != modTime && jEdit.getBooleanProperty("view.checkModStatus")) {
            Object[] args = { this.path };
            int result = GUIUtilities.confirm(view, "filechanged-save", args, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result != JOptionPane.YES_OPTION)
                return false;
        }
    }
    EditBus.send(new BufferUpdate(this, view, BufferUpdate.SAVING));
    setPerformingIO(true);
    final String oldPath = this.path;
    final String oldSymlinkPath = symlinkPath;
    final String newPath = path == null ? this.path : path;
    VFS vfs = VFSManager.getVFSForPath(newPath);
    if (!checkFileForSave(view, vfs, newPath)) {
        setPerformingIO(false);
        return false;
    }
    Object session = vfs.createVFSSession(newPath, view);
    if (session == null) {
        setPerformingIO(false);
        return false;
    }
    unsetProperty("overwriteReadonly");
    unsetProperty("forbidTwoStageSave");
    try {
        VFSFile file = vfs._getFile(session, newPath, view);
        if (file != null) {
            boolean vfsRenameCap = (vfs.getCapabilities() & VFS.RENAME_CAP) != 0;
            if (!file.isWriteable()) {
                Log.log(Log.WARNING, this, "Buffer saving : File " + file + " is readOnly");
                if (vfsRenameCap) {
                    Log.log(Log.DEBUG, this, "Buffer saving : VFS can rename files");
                    String savePath = vfs._canonPath(session, newPath, view);
                    if (!MiscUtilities.isURL(savePath))
                        savePath = MiscUtilities.resolveSymlinks(savePath);
                    savePath = vfs.getTwoStageSaveName(savePath);
                    if (savePath == null) {
                        Log.log(Log.DEBUG, this, "Buffer saving : two stage save impossible because path is null");
                        VFSManager.error(view, newPath, "ioerror.save-readonly-twostagefail", null);
                        setPerformingIO(false);
                        return false;
                    } else {
                        int result = GUIUtilities.confirm(view, "vfs.overwrite-readonly", new Object[] { newPath }, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
                            Log.log(Log.WARNING, this, "Buffer saving : two stage save will be used to save buffer");
                            setBooleanProperty("overwriteReadonly", true);
                        } else {
                            Log.log(Log.DEBUG, this, "Buffer not saved");
                            setPerformingIO(false);
                            return false;
                        }
                    }
                } else {
                    Log.log(Log.WARNING, this, "Buffer saving : file is readonly and vfs cannot do two stage save");
                    VFSManager.error(view, newPath, "ioerror.write-error-readonly", null);
                    setPerformingIO(false);
                    return false;
                }
            } else {
                String savePath = vfs._canonPath(session, newPath, view);
                if (!MiscUtilities.isURL(savePath))
                    savePath = MiscUtilities.resolveSymlinks(savePath);
                savePath = vfs.getTwoStageSaveName(savePath);
                if (jEdit.getBooleanProperty("twoStageSave") && (!vfsRenameCap || savePath == null)) {
                    // the file is writeable but the vfs cannot do two stage. We must overwrite  
                    // readonly flag  
                    int result = GUIUtilities.confirm(view, "vfs.twostageimpossible", new Object[] { newPath }, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        Log.log(Log.WARNING, this, "Buffer saving : two stage save cannot be used");
                        setBooleanProperty("forbidTwoStageSave", true);
                    } else {
                        Log.log(Log.DEBUG, this, "Buffer not saved");
                        setPerformingIO(false);
                        return false;
                    }
                }
            }
        }
    } catch (IOException io) {
        VFSManager.error(view, newPath, "ioerror", new String[] { io.toString() });
        setPerformingIO(false);
        return false;
    } finally {
        try {
            vfs._endVFSSession(session, view);
        } catch (IOException io) {
            VFSManager.error(view, newPath, "ioerror", new String[] { io.toString() });
            setPerformingIO(false);
            return false;
        }
    }
    if (!vfs.save(view, this, newPath)) {
        setPerformingIO(false);
        return false;
    }
    // Once save is complete, do a few other things  
    VFSManager.runInAWTThread(new Runnable() {

        public void run() {
            setPerformingIO(false);
            setProperty("overwriteReadonly", null);
            finishSaving(view, oldPath, oldSymlinkPath, newPath, rename, getBooleanProperty(BufferIORequest.ERROR_OCCURRED));
            updateMarkersFile(view);
        }
    });
    int check = jEdit.getIntegerProperty("checkFileStatus");
    if (!disableFileStatusCheck && (check == GeneralOptionPane.checkFileStatus_all || check == GeneralOptionPane.checkFileStatus_operations))
        jEdit.checkBufferStatus(view, false);
    return true;
}
