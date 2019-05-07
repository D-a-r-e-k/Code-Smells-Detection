//}}}  
//{{{ getAutoReloadDialog() method  
/**
	 * Returns the status of the AUTORELOAD_DIALOG flag
	 * If true, prompt for reloading or notify user
	 * when the file has changed on disk
	 */
public boolean getAutoReloadDialog() {
    return getFlag(AUTORELOAD_DIALOG);
}
