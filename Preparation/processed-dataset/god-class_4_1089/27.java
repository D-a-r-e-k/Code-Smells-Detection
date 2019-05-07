//}}}  
//{{{ setNewFile() method  
/**
	 * Sets the new file flag.
	 * @param newFile The new file flag
	 */
public void setNewFile(boolean newFile) {
    setFlag(NEW_FILE, newFile);
    if (!newFile)
        setFlag(UNTITLED, false);
}
