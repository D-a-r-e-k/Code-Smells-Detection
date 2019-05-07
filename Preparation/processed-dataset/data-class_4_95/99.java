/**
	 * Tries to find the Client by the matching pattern passed to the key.
	 * <p>
	 * Note: This command is an expensive so do use it limitedly and
	 * cache the returned object. But before you start sending message to the 
	 * cached object do validate that ClientHandler with you is currently 
	 * connected and is pointing to the same clinet has it was before.
	 * This can be done as follows. <pre>
	foundClientHandler.isConnected(); //this method will through SocketException if not connected
	Date newTime = foundClientHandler.getClientConnectedTime();
	if(oldCachedTime!=newTime) {
		//Client had disconnected and ClientHandler was reused for
		//some other client, so write code to again find ur client
		foundClientHandler = handler.getServer().findFirstClientByKey("friendsid");
		...
	}</pre>
	 * </p>
	 * @see ClientIdentifiable
	 * @return ClientHandler object if client was found else <code>null</code>
	 * @since 1.4
	 */
public Iterator findAllClientByKey(String pattern) {
    return clientIdentifier.findAllClientByKey(pattern);
}
