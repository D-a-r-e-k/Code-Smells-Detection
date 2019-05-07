/**
	 * Starts server in non-blocking mode.
	 * @since 1.4.5
	 */
private void runNonBlocking(TheClient theClient) throws Exception {
    int selectCount = 0;
    Iterator iterator = null;
    SelectionKey key = null;
    ServerSocketChannel serverChannel = null;
    SocketChannel socketChannel = null;
    Socket client = null;
    ClientHandler _chPolled = null;
    boolean stopServerProcessed = false;
    int linger = getBasicConfig().getAdvancedSettings().getSocketLinger();
    registerChannelRequestMap = new HashMap();
    while (true) {
        selectCount = selector.select(500);
        //selectCount = selector.select();//for testing  
        //check for any pending registerChannel req.  
        synchronized (registerChannelRequestMap) {
            if (registerChannelRequestMap.size() > 0) {
                RegisterChannelRequest req = null;
                Object hashkey = null;
                iterator = registerChannelRequestMap.keySet().iterator();
                while (iterator.hasNext()) {
                    hashkey = iterator.next();
                    req = (RegisterChannelRequest) registerChannelRequestMap.get(hashkey);
                    req.register(getSelector());
                }
                iterator = null;
                registerChannelRequestMap.clear();
            }
        }
        //sync  
        if (stopServer == true && stopServerProcessed == false) {
            logger.warning("Closing " + getName());
            serverSocketChannel.close();
            stopServerProcessed = true;
            server = null;
            serverSocketChannel = null;
            setServiceState(Service.STOPPED);
            logger.warning("Closed " + getName());
            processServerHooks(ServerHook.POST_SHUTDOWN);
        }
        if (stopServer == false && stopServerProcessed == true) {
            logger.finest("Server must have re-started.. will break");
            break;
        }
        if (selectCount == 0 && stopServerProcessed == true) {
            java.util.Set keyset = selector.keys();
            if (keyset.isEmpty() == true && getClientCount() <= 0) {
                break;
            } else {
                continue;
            }
        } else if (selectCount == 0) {
            continue;
        }
        iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            key = (SelectionKey) iterator.next();
            if (key.isValid() == false) {
                iterator.remove();
                continue;
            }
            if (key.isAcceptable() && stopServer == false) {
                logger.finest("Key is Acceptable");
                serverChannel = (ServerSocketChannel) key.channel();
                socketChannel = serverChannel.accept();
                if (socketChannel == null) {
                    iterator.remove();
                    continue;
                }
                client = socketChannel.socket();
                if (linger < 0) {
                    client.setSoLinger(false, 0);
                } else {
                    client.setSoLinger(true, linger);
                }
                if (checkAccessConstraint(client) == false) {
                    iterator.remove();
                    continue;
                }
                socketChannel.configureBlocking(false);
                theClient.setTrusted(getSkipValidation());
                theClient.setSocket(socketChannel.socket());
                theClient.setSocketChannel(socketChannel);
                if (clientDataClass != null) {
                    if (getClientDataPool() == null) {
                        clientData = (ClientData) clientDataClass.newInstance();
                    } else {
                        //borrow a object from pool  
                        clientData = (ClientData) getClientDataPool().borrowObject();
                    }
                    theClient.setClientData(clientData);
                }
                //Check if max connection has reached  
                if (getSkipValidation() != true && maxConnection != -1 && getClientHandlerPool().getNumActive() >= maxConnection) {
                    theClient.setClientEvent(ClientEvent.MAX_CON);
                } else {
                    theClient.setClientEvent(ClientEvent.ACCEPT);
                }
                try {
                    _chPolled = (ClientHandler) getClientHandlerPool().borrowObject();
                    logger.finest("Asking " + _chPolled.getName() + " to handle.");
                    _chPolled.handleClient(theClient);
                } catch (java.util.NoSuchElementException nsee) {
                    logger.warning("Could not borrow ClientHandler Object from pool. Error: " + nsee);
                    logger.warning("Closing SocketChannel [" + serverChannel.socket() + "] since no ClientHandler available.");
                    socketChannel.close();
                }
                if (_chPolled != null) {
                    try {
                        getClientPool().addClient(_chPolled, true);
                    } catch (java.util.NoSuchElementException nsee) {
                        logger.warning("Could not borrow Thread from pool. Error: " + nsee);
                    }
                    _chPolled = null;
                }
                socketChannel = null;
                client = null;
                setSkipValidation(false);
            } else if (key.isValid() && key.isReadable()) {
                boolean addedEvent = false;
                ClientHandler _ch = null;
                try {
                    _ch = (ClientHandler) key.attachment();
                    logger.finest("Key is Readable, removing OP_READ from interestOps for " + _ch.getName());
                    key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                    _ch.addEvent(ClientEvent.READ);
                    addedEvent = true;
                    //_ch.setSelectionKey(key);  
                    getClientPool().addClient(_ch);
                } catch (CancelledKeyException cke) {
                    logger.fine("Ignored Error - Key was Cancelled: " + cke);
                } catch (java.util.NoSuchElementException nsee) {
                    logger.finest("NoSuchElementException: " + nsee);
                    if (addedEvent)
                        _ch.removeEvent(ClientEvent.READ);
                    continue;
                }
                _ch = null;
            } else if (key.isValid() && key.isWritable()) {
                if (getClientPool().shouldNioWriteHappen() == false) {
                    continue;
                }
                boolean addedEvent = false;
                ClientHandler _ch = null;
                try {
                    _ch = (ClientHandler) key.attachment();
                    logger.finest("Key is Writable, removing OP_WRITE from interestOps for " + _ch.getName());
                    //remove OP_WRITE from interest set  
                    key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                    _ch.addEvent(ClientEvent.WRITE);
                    addedEvent = true;
                    //_ch.setSelectionKey(key);  
                    getClientPool().addClient(_ch);
                } catch (CancelledKeyException cke) {
                    logger.fine("Ignored Error - Key was Cancelled: " + cke);
                } catch (java.util.NoSuchElementException nsee) {
                    logger.finest("NoSuchElementException: " + nsee);
                    if (addedEvent)
                        _ch.removeEvent(ClientEvent.WRITE);
                    continue;
                }
                _ch = null;
            } else if (stopServer == true && key.isAcceptable()) {
                //we will not accept this key  
                setSkipValidation(false);
            } else {
                logger.warning("Unknown key got in SelectionKey: " + key);
            }
            iterator.remove();
            //Remove key  
            Thread.yield();
        }
        //end of iterator  
        iterator = null;
    }
}
