/**
	 * Returns ObjectPool of {@link org.quickserver.net.server.ClientHandler} 
	 * class.
	 * @exception IllegalStateException if pool is not created yet.
	 * @since 1.3
	 */
public ObjectPool getClientHandlerPool() {
    if (clientHandlerPool == null)
        throw new IllegalStateException("No ClientHandler Pool available yet!");
    return clientHandlerPool;
}
