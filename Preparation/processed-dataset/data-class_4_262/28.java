//}}}  
//{{{ isUntitled() method  
/**
	 * Returns true if this file is 'untitled'. This method is thread-safe.
	 */
public boolean isUntitled() {
    return getFlag(UNTITLED);
}
