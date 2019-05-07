/**
	 * Check if the buffer has changed on disk.
	 * @return One of <code>NOT_CHANGED</code>, <code>CHANGED</code>, or
	 * <code>DELETED</code>.
	 *
	 * @since jEdit 4.2pre1
	 */
public int checkFileStatus(View view) {
    // - don't do these checks while a save is in progress,  
    // because for a moment newModTime will be greater than  
    // oldModTime, due to the multithreading  
    // - only supported on local file system  
    if (!isPerformingIO() && file != null && !getFlag(NEW_FILE)) {
        boolean newReadOnly = file.exists() && !file.canWrite();
        if (newReadOnly != isFileReadOnly()) {
            setFileReadOnly(newReadOnly);
            EditBus.send(new BufferUpdate(this, null, BufferUpdate.DIRTY_CHANGED));
        }
        long oldModTime = modTime;
        long newModTime = file.lastModified();
        if (newModTime != oldModTime) {
            modTime = newModTime;
            if (!file.exists()) {
                setFlag(NEW_FILE, true);
                setDirty(true);
                return FILE_DELETED;
            } else {
                return FILE_CHANGED;
            }
        }
    }
    return FILE_NOT_CHANGED;
}
