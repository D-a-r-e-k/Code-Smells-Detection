public void run() {
    exceptionInRun = null;
    TheClient theClient = initTheClient();
    try {
        stopServer = false;
        closeAllPools();
        initAllPools();
        makeServerSocket();
        System.out.println(serverBanner);
        //print banner  
        setServiceState(Service.RUNNING);
        //v1.2  
        processServerHooks(ServerHook.POST_STARTUP);
        //v1.3.3  
        if (getBlockingMode() == false) {
            runNonBlocking(theClient);
            if (stopServer == true) {
                logger.finest("Closing selector for " + getName());
                selector.close();
            }
            return;
        } else {
            runBlocking(theClient);
        }
    } catch (BindException e) {
        exceptionInRun = e;
        logger.severe(getName() + " BindException for Port " + getPort() + " @ " + getBindAddr().getHostAddress() + " : " + e.getMessage());
    } catch (javax.net.ssl.SSLException e) {
        exceptionInRun = e;
        logger.severe("SSLException " + e);
        logger.fine("StackTrace:\n" + MyString.getStackTrace(e));
    } catch (IOException e) {
        exceptionInRun = e;
        logger.severe("IOError " + e);
        logger.fine("StackTrace:\n" + MyString.getStackTrace(e));
    } catch (Exception e) {
        exceptionInRun = e;
        logger.severe("Error " + e);
        logger.fine("StackTrace:\n" + MyString.getStackTrace(e));
    } finally {
        if (getBlockingMode() == true) {
            logger.warning("Closing " + getName());
            try {
                if (isClosed() == false) {
                    server.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            server = null;
            serverSocketChannel = null;
            setServiceState(Service.STOPPED);
            logger.warning("Closed " + getName());
            processServerHooks(ServerHook.POST_SHUTDOWN);
        } else if (getBlockingMode() == false && exceptionInRun != null) {
            logger.warning("Closing " + getName() + " - Had Error: " + exceptionInRun);
            try {
                if (isClosed() == false) {
                    if (serverSocketChannel != null)
                        serverSocketChannel.close();
                    if (server != null)
                        server.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            server = null;
            serverSocketChannel = null;
            setServiceState(Service.STOPPED);
            logger.warning("Closed " + getName());
            processServerHooks(ServerHook.POST_SHUTDOWN);
        }
    }
}
