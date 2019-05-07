//}}}  
//{{{ getTabSize() method  
/**
	 * Returns the tab size used in this buffer. This is equivalent
	 * to calling <code>getProperty("tabSize")</code>.
	 * This method is thread-safe.
	 */
public int getTabSize() {
    int tabSize = getIntegerProperty("tabSize", 8);
    if (tabSize <= 0)
        return 8;
    else
        return tabSize;
}
