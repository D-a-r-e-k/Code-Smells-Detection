/**
	 * Returns ObjectPool of {@link org.quickserver.net.server.ClientData} 
	 * class. If ClientData was not poolable will return  null.
	 * @since 1.3
	 */
public ObjectPool getClientDataPool() {
    return clientDataPool;
}
