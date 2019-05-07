//}}}  
//{{{ insertString() method  
/**
	 * Insert a string into the buffer
	 * @param offset The offset
	 * @param str The string
	 * @param attr ignored
	 * @deprecated Call <code>insert()</code> instead.
	 */
@Deprecated
public void insertString(int offset, String str, AttributeSet attr) {
    insert(offset, str);
}
