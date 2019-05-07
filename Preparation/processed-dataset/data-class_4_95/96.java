/**
	 * Returns an iterator containing all the 
	 * {@link org.quickserver.net.server.ClientHandler} that
	 * are currently handling clients. 
	 * It is recommended not to change the collection under an iterator. 
	 *
	 * It is imperative that the user manually synchronize on the returned collection 
	 * when iterating over it: 
	 * <code><pre>
   Eg:

	ClientData foundClientData = null;
	Object syncObj = quickserver.getClientIdentifier().getObjectToSynchronize();
	synchronized(syncObj) {	
		Iterator iterator = quickserver.findAllClient();
		while(iterator.hasNext()) {
			foundClientHandler = (ClientHandler) iterator.next();
			....
		}
	}

	//OR

	ClientData foundClientData = null;
	ClientIdentifier clientIdentifier = quickserver.getClientIdentifier();
	synchronized(clientIdentifier.getObjectToSynchronize()) {	
		Iterator iterator = clientIdentifier.findAllClient();
		while(iterator.hasNext()) {
			foundClientHandler = (ClientHandler) iterator.next();
			....
		}
	}
   </code></pre>
	 * @since 1.3.1
	 */
public Iterator findAllClient() {
    return clientIdentifier.findAllClient();
}
