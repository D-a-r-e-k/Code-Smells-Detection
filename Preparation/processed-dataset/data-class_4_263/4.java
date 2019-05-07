//}}}  
//{{{ isPerformingIO() method  
/**
	 * Returns true if the buffer is currently performing I/O.
	 * This method is thread-safe.
	 * @since jEdit 2.7pre1
	 */
public boolean isPerformingIO() {
    return isLoading() || io;
}
