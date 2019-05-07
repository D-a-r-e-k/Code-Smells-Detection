/**
	 * Returns a ServerSocket object to be used for listening.
	 * @since 1.4.0
	 */
protected void makeServerSocket() throws BindException, IOException {
    server = null;
    logger.finest("Binding " + getName() + " to IP: " + getBindAddr());
    InetSocketAddress bindAddress = new InetSocketAddress(getBindAddr(), getPort());
    try {
        NetworkInterface ni = NetworkInterface.getByInetAddress(getBindAddr());
        if (ni != null) {
            logger.fine("NetworkInterface: " + ni);
        }
    } catch (Exception igrnore) {
    } catch (Error igrnore) {
    }
    if (getSecure().isEnable() == false) {
        logger.fine("Making a normal ServerSocket for " + getName());
        setRunningSecure(false);
        if (getBlockingMode() == false) {
            //for non-blocking  
            serverSocketChannel = ServerSocketChannel.open();
            server = serverSocketChannel.socket();
            server.bind(bindAddress, getBasicConfig().getAdvancedSettings().getBacklog());
        } else {
            //for blocking  
            server = new ServerSocket(getPort(), getBasicConfig().getAdvancedSettings().getBacklog(), getBindAddr());
        }
    } else {
        logger.fine("Making a secure ServerSocket for " + getName());
        try {
            ServerSocketFactory ssf = getSSLContext().getServerSocketFactory();
            SSLServerSocket serversocket = (SSLServerSocket) ssf.createServerSocket(getPort(), getBasicConfig().getAdvancedSettings().getBacklog(), getBindAddr());
            serversocket.setNeedClientAuth(secure.isClientAuthEnable());
            setRunningSecure(true);
            secureStoreManager.logSSLServerSocketInfo(serversocket);
            server = serversocket;
            serverSocketChannel = server.getChannel();
            if (serverSocketChannel == null && getBlockingMode() == false) {
                logger.warning("Secure Server does not support Channel! So will run in blocking mode.");
                blockingMode = true;
            }
        } catch (NoSuchAlgorithmException e) {
            logger.warning("NoSuchAlgorithmException : " + e);
            throw new IOException("Error creating secure socket : " + e.getMessage());
        } catch (KeyManagementException e) {
            logger.warning("KeyManagementException : " + e);
            throw new IOException("Error creating secure socket : " + e.getMessage());
        }
    }
    server.setReuseAddress(true);
    if (getBlockingMode() == false) {
        logger.fine("Server Mode " + getName() + " - Non Blocking");
        if (selector == null || selector.isOpen() == false) {
            logger.finest("Opening new selector");
            selector = Selector.open();
        } else {
            logger.finest("Reusing selector: " + selector);
        }
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selector.wakeup();
    } else {
        logger.fine("Server Mode " + getName() + " - Blocking");
    }
}
