//}}}  
//{{{ getBufferListeners() method  
/**
	 * Returns an array of registered buffer change listeners.
	 * @since jEdit 4.3pre3
	 */
public BufferListener[] getBufferListeners() {
    BufferListener[] returnValue = new BufferListener[bufferListeners.size()];
    for (int i = 0; i < returnValue.length; i++) {
        returnValue[i] = bufferListeners.get(i).listener;
    }
    return returnValue;
}
