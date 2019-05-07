//}}}  
//{{{ getIndentSize() method  
/**
	 * Returns the indent size used in this buffer. This is equivalent
	 * to calling <code>getProperty("indentSize")</code>.
	 * This method is thread-safe.
	 * @since jEdit 2.7pre1
	 */
public int getIndentSize() {
    int indentSize = getIntegerProperty("indentSize", 8);
    if (indentSize <= 0)
        return 8;
    else
        return indentSize;
}
