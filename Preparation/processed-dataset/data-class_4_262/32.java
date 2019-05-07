//}}}  
//}}}  
//{{{ Buffer events  
//{{{ addBufferChangeListener() method  
/**
	 * @deprecated Call {@link JEditBuffer#addBufferListener(BufferListener,int)}.
	 */
@Deprecated
public void addBufferChangeListener(BufferChangeListener listener, int priority) {
    addBufferListener(new BufferChangeListener.Adapter(listener), priority);
}
