/**
	 * Stops the QuickServer.
	 *
	 * @exception org.quickserver.net.AppException 
	 *  if could not stop server
	 * @since 1.1
	 * @see #stopService
	 */
public void stopServer() throws AppException {
    processServerHooks(ServerHook.PRE_SHUTDOWN);
    logger.warning("Stopping " + getName());
    stopServer = true;
    Socket death = null;
    if (isClosed() == true) {
        logger.warning("Server " + getName() + " is not running!");
        throw new AppException("Server " + getName() + " is not running!");
    }
    try {
        if (getBlockingMode() == true) {
            if (getSecure().isEnable() == false) {
                death = new Socket(server.getInetAddress(), server.getLocalPort());
                death.getInputStream().read();
                death.close();
            } else {
                death = getSSLSocketFactory().createSocket(server.getInetAddress(), server.getLocalPort());
                Thread.currentThread().sleep(100);
                death.close();
            }
        }
        if (serverSocketChannel != null) {
            serverSocketChannel.close();
        }
    } catch (IOException e) {
        logger.fine("IOError stopping " + getName() + ": " + e);
    } catch (Exception e) {
        logger.warning("Error stopping " + getName() + ": " + e);
        throw new AppException("Error in stopServer " + getName() + ": " + e);
    }
    for (int i = 0; getServiceState() != Service.STOPPED; i++) {
        try {
            Thread.sleep(60);
        } catch (Exception e) {
            logger.warning("Error waiting for " + getName() + " to fully stop. Error: " + e);
        }
        if (i > 1000) {
            logger.severe("Server was not stopped even after 10sec.. will terminate now.");
            System.exit(-1);
        }
    }
    if (adminServer == null || getQSAdminServer().getServer() != this) {
        //so this is not qsadmin  
        setClassLoader(null);
    }
    logger.info("Stopped " + getName());
}
