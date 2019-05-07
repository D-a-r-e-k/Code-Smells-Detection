//}}}  
//{{{ removeBufferListener() method  
/**
	 * Removes a buffer change listener.
	 * @param listener The listener
	 * @since jEdit 4.3pre3
	 */
public void removeBufferListener(BufferListener listener) {
    for (int i = 0; i < bufferListeners.size(); i++) {
        if (bufferListeners.get(i).listener == listener) {
            bufferListeners.remove(i);
            return;
        }
    }
}
