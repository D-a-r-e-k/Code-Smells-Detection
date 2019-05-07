/**
	 * Adds a buffer change listener.
	 * @param listener The listener
	 * @since jEdit 4.3pre3
	 */
public void addBufferListener(BufferListener listener) {
    addBufferListener(listener, NORMAL_PRIORITY);
}
