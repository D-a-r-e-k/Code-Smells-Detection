//{{{ addBufferListener() methods  
/**
	 * Adds a buffer change listener.
	 * @param listener The listener
	 * @param priority Listeners with HIGH_PRIORITY get the event before
	 * listeners with NORMAL_PRIORITY
	 * @since jEdit 4.3pre3
	 */
public void addBufferListener(BufferListener listener, int priority) {
    Listener l = new Listener(listener, priority);
    for (int i = 0; i < bufferListeners.size(); i++) {
        Listener _l = bufferListeners.get(i);
        if (_l.priority < priority) {
            bufferListeners.add(i, l);
            return;
        }
    }
    bufferListeners.add(l);
}
