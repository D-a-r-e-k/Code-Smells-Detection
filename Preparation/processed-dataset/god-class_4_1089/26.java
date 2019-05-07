//}}}  
//{{{ isNewFile() method  
/**
	 * Returns whether this buffer lacks a corresponding version on disk.
	 * This method is thread-safe.
	 */
public boolean isNewFile() {
    return getFlag(NEW_FILE);
}
