/**
	 * Creates a new ByteBufferOutputStream using the given list as its base
	 * and ClientHandler as the target channel.
	 */
public ByteBufferOutputStream(ArrayList bufferList, ClientHandler handler) {
    if (bufferList == null || handler == null)
        throw new IllegalArgumentException("ArrayList or ClientHandler was null.");
    this.bufferList = bufferList;
    this.handler = (NonBlockingClientHandler) handler;
}
