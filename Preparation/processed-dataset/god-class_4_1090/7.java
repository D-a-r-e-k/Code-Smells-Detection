//}}}  
//{{{ isReadOnly() method  
/**
	 * Returns true if this file is read only, false otherwise.
	 * This method is thread-safe.
	 */
public boolean isReadOnly() {
    return readOnly || readOnlyOverride;
}
