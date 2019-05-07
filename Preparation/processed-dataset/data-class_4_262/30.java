//}}}  
//{{{ isTemporary() method  
/**
	 * Returns if this is a temporary buffer. This method is thread-safe.
	 * @see jEdit#openTemporary(View,String,String,boolean)
	 * @see jEdit#commitTemporary(Buffer)
	 * @since jEdit 2.2pre7
	 */
public boolean isTemporary() {
    return getFlag(TEMPORARY);
}
