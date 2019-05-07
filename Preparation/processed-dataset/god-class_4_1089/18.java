//}}}  
//{{{ removeAutosaveFile() method  
/**
	 * Remove the autosave file.
	 * @since jEdit 4.3pre12
	 */
public void removeAutosaveFile() {
    if (autosaveFile != null) {
        autosaveFile.delete();
        setFlag(AUTOSAVE_DIRTY, true);
    }
}
