//}}}  
//{{{ Flags  
//{{{ isDirty() method  
/**
	 * Returns whether there have been unsaved changes to this buffer.
	 * This method is thread-safe.
	 */
public boolean isDirty() {
    return dirty;
}
