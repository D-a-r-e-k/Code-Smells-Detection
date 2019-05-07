//}}}  
//{{{ isClosed() method  
/**
	 * Returns true if this buffer has been closed with
	 * {@link org.gjt.sp.jedit.jEdit#closeBuffer(View,Buffer)}.
	 * This method is thread-safe.
	 */
public boolean isClosed() {
    return getFlag(CLOSED);
}
