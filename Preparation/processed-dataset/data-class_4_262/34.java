//}}}  
//{{{ removeBufferChangeListener() method  
/**
	 * @deprecated Call {@link JEditBuffer#removeBufferListener(BufferListener)}.
	 */
@Deprecated
public void removeBufferChangeListener(BufferChangeListener listener) {
    BufferListener[] listeners = getBufferListeners();
    for (int i = 0; i < listeners.length; i++) {
        BufferListener l = listeners[i];
        if (l instanceof BufferChangeListener.Adapter) {
            if (((BufferChangeListener.Adapter) l).getDelegate() == listener) {
                removeBufferListener(l);
                return;
            }
        }
    }
}
