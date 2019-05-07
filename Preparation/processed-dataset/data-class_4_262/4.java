//}}}  
//{{{ autosave() method  
/**
	 * Autosaves this buffer.
	 */
public void autosave() {
    if (autosaveFile == null || !getFlag(AUTOSAVE_DIRTY) || !isDirty() || isPerformingIO() || !autosaveFile.getParentFile().exists())
        return;
    setFlag(AUTOSAVE_DIRTY, false);
    VFSManager.runInWorkThread(new BufferAutosaveRequest(null, this, null, VFSManager.getFileVFS(), autosaveFile.getPath()));
}
