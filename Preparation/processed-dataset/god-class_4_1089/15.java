//}}}  
//{{{ setAutoReloadDialog() method  
/**
	 * Sets the status of the AUTORELOAD_DIALOG flag
	 * @param value # If true, prompt for reloading or notify user
	 * when the file has changed on disk

	 */
public void setAutoReloadDialog(boolean value) {
    setFlag(AUTORELOAD_DIALOG, value);
}
