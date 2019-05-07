//}}}  
//{{{ addBufferChangeListener() method  
/**
	 * @deprecated Call {@link JEditBuffer#addBufferListener(BufferListener)}.
	 */
@Deprecated
public void addBufferChangeListener(BufferChangeListener listener) {
    addBufferListener(new BufferChangeListener.Adapter(listener), NORMAL_PRIORITY);
}
