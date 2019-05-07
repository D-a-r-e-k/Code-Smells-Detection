//}}}  
//{{{ getLineCount() method  
/**
	 * Returns the number of physical lines in the buffer.
	 * This method is thread-safe.
	 * @since jEdit 3.1pre1
	 */
public int getLineCount() {
    // no need to lock since this just returns a value and that's it  
    return lineMgr.getLineCount();
}
