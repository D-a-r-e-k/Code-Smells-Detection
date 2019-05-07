/**
	 * Tries to find the Client by the Key passed.
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
		//someother client, so write code to again find ur client
		foundClientHandler = handler.getServer().findClientByKey("friendskey");
		...
	}</pre>
	 * </p>
	 * @see ClientIdentifiable
	 * @return ClientHandler object if client was found else <code>null</code>
	 * @since 1.3.1
	 */
public ClientHandler findClientByKey(String key) {
    return clientIdentifier.findClientByKey(key);
}
