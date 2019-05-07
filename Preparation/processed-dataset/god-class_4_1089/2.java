//}}}  
//{{{ load() method  
/**
	 * Loads the buffer from disk.
	 * @param view The view
	 * @param reload If true, user will not be asked to recover autosave
	 * file, if any
	 *
	 * @since 2.5pre1
	 */
public boolean load(final View view, final boolean reload) {
    if (isPerformingIO()) {
        GUIUtilities.error(view, "buffer-multiple-io", null);
        return false;
    }
    setBooleanProperty(BufferIORequest.ERROR_OCCURRED, false);
    setLoading(true);
    // view text areas temporarily blank out while a buffer is  
    // being loaded, to indicate to the user that there is no  
    // data available yet.  
    if (!getFlag(TEMPORARY))
        EditBus.send(new BufferUpdate(this, view, BufferUpdate.LOAD_STARTED));
    final boolean loadAutosave;
    if (reload || !getFlag(NEW_FILE)) {
        if (file != null)
            modTime = file.lastModified();
        // Only on initial load  
        if (!reload && autosaveFile != null && autosaveFile.exists())
            loadAutosave = recoverAutosave(view);
        else {
            if (autosaveFile != null)
                autosaveFile.delete();
            loadAutosave = false;
        }
        if (!loadAutosave) {
            VFS vfs = VFSManager.getVFSForPath(path);
            if (!checkFileForLoad(view, vfs, path)) {
                setLoading(false);
                return false;
            }
            // have to check again since above might set  
            // NEW_FILE flag  
            if (reload || !getFlag(NEW_FILE)) {
                if (!vfs.load(view, this, path)) {
                    setLoading(false);
                    return false;
                }
            }
        }
    } else
        loadAutosave = false;
    //{{{ Do some stuff once loading is finished  
    Runnable runnable = new Runnable() {

        public void run() {
            String newPath = getStringProperty(BufferIORequest.NEW_PATH);
            Segment seg = (Segment) getProperty(BufferIORequest.LOAD_DATA);
            IntegerArray endOffsets = (IntegerArray) getProperty(BufferIORequest.END_OFFSETS);
            loadText(seg, endOffsets);
            unsetProperty(BufferIORequest.LOAD_DATA);
            unsetProperty(BufferIORequest.END_OFFSETS);
            unsetProperty(BufferIORequest.NEW_PATH);
            undoMgr.clear();
            undoMgr.setLimit(jEdit.getIntegerProperty("buffer.undoCount", 100));
            if (!getFlag(TEMPORARY))
                finishLoading();
            setLoading(false);
            // if reloading a file, clear dirty flag  
            if (reload)
                setDirty(false);
            if (!loadAutosave && newPath != null)
                setPath(newPath);
            // if loadAutosave is false, we loaded an  
            // autosave file, so we set 'dirty' to true  
            // note that we don't use setDirty(),  
            // because a) that would send an unnecessary  
            // message, b) it would also set the  
            // AUTOSAVE_DIRTY flag, which will make  
            // the autosave thread write out a  
            // redundant autosave file  
            if (loadAutosave)
                Buffer.super.setDirty(true);
            // send some EditBus messages  
            if (!getFlag(TEMPORARY)) {
                fireBufferLoaded();
                EditBus.send(new BufferUpdate(Buffer.this, view, BufferUpdate.LOADED));
            }
        }
    };
    //}}}  
    if (getFlag(TEMPORARY))
        runnable.run();
    else
        VFSManager.runInAWTThread(runnable);
    return true;
}
