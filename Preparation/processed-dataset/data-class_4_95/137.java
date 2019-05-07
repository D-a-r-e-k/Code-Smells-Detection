/**
	 * Starts server in blocking mode.
	 * @since 1.4.5
	 */
private void runBlocking(TheClient theClient) throws Exception {
    Socket client = null;
    ClientHandler _chPolled = null;
    int linger = getBasicConfig().getAdvancedSettings().getSocketLinger();
    //long stime = System.currentTimeMillis();  
    //long etime = System.currentTimeMillis();  
    while (true) {
        //etime = System.currentTimeMillis();  
        //System.out.println("Time Taken: "+(etime-stime));  
        client = server.accept();
        //stime = System.currentTimeMillis();  
        if (linger < 0) {
            client.setSoLinger(false, 0);
        } else {
            client.setSoLinger(true, linger);
        }
        if (stopServer) {
            //Client connected when server was about to be shutdown.  
            try {
                client.close();
            } catch (Exception e) {
            }
            break;
        }
        if (checkAccessConstraint(client) == false) {
            continue;
        }
        //Check if max connection has reached  
        if (getSkipValidation() != true && maxConnection != -1 && getClientHandlerPool().getNumActive() >= maxConnection) {
            theClient.setClientEvent(ClientEvent.MAX_CON_BLOCKING);
        } else {
            theClient.setClientEvent(ClientEvent.RUN_BLOCKING);
        }
        theClient.setTrusted(getSkipValidation());
        theClient.setSocket(client);
        theClient.setSocketChannel(client.getChannel());
        //mostly null  
        if (clientDataClass != null) {
            if (getClientDataPool() == null) {
                clientData = (ClientData) clientDataClass.newInstance();
            } else {
                clientData = (ClientData) getClientDataPool().borrowObject();
            }
            theClient.setClientData(clientData);
        }
        try {
            _chPolled = (ClientHandler) getClientHandlerPool().borrowObject();
            _chPolled.handleClient(theClient);
        } catch (java.util.NoSuchElementException nsee) {
            logger.warning("Could not borrow ClientHandler from pool. Error: " + nsee);
            logger.warning("Closing Socket [" + client + "] since no ClientHandler available.");
            client.close();
        }
        if (_chPolled != null) {
            try {
                getClientPool().addClient(_chPolled, true);
            } catch (java.util.NoSuchElementException nsee) {
                logger.warning("Could not borrow Thread from pool. Error: " + nsee);
            }
            _chPolled = null;
        }
        client = null;
        //reset it back  
        setSkipValidation(false);
    }
}
