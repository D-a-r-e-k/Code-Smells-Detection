//}}}  
//{{{ isEditable() method  
/**
	 * Returns true if this file is editable, false otherwise. A file may
	 * become uneditable if it is read only, or if I/O is in progress.
	 * This method is thread-safe.
	 * @since jEdit 2.7pre1
	 */
public boolean isEditable() {
    return !(isReadOnly() || isPerformingIO());
}
