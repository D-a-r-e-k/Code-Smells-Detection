/**
   * This method connects the Store, and sets the StoreInfo to know that
   * the Store should be connected.  You should use this method instead of
   * calling getStore().connect(), because if you use this method, then
   * the StoreInfo will try to keep the Store connected, and will try to
   * reconnect the Store if it gets disconnected before
   * disconnectStore is called.
   *
   * This method also calls updateChildren() to load the children of
   * the Store, if the children vector has not been loaded yet.
   */
public void connectStore() throws MessagingException, OperationCancelledException {
    getLogger().log(Level.FINE, "trying to connect store " + getStoreID());
    if (store.isConnected()) {
        getLogger().log(Level.FINE, "store " + getStoreID() + " is already connected.");
        connected = true;
        return;
    } else {
        // test the connection and execute the precommand, if any. 
        testConnection();
        executePrecommand();
        getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  doing store.connect()");
        boolean connectSucceeded = false;
        while (!connectSucceeded) {
            try {
                getLogger().fine("running store.connect()");
                store.connect();
                connectSucceeded = true;
                mPreferredStatus = FolderInfo.CONNECTED;
                getLogger().fine("done with store.connect().");
                // if authentication is necessary, then the authenticator will 
                // show, so will need to be closed. 
                mAuthenticator.disposeAuthenticator();
            } catch (MessagingException me) {
                getLogger().fine("caught exception.");
                // cases here: 
                // 1)  authenticator cancelled. 
                if (mAuthenticator.isCancelled()) {
                    getLogger().fine("operation was cancelled.");
                    mAuthenticator.disposeAuthenticator();
                    mPreferredStatus = FolderInfo.DISCONNECTED;
                    throw new OperationCancelledException();
                }
                // 2) Interrupted IO exception -- try again. 
                Exception nextEx = me.getNextException();
                if (nextEx != null && nextEx instanceof java.io.InterruptedIOException) {
                    getLogger().fine("retrying--interruptedioexception");
                } else {
                    // 3) TLS exception -- fall back. 
                    if (nextEx != null && nextEx.toString().contains("SunCertPathBuilderException") && "tls".equalsIgnoreCase(sslSetting)) {
                        getLogger().fine("falling back to no tls.");
                        // fall back. 
                        Properties p = mSession.getProperties();
                        p.setProperty("mail.imap.starttls.enable", "false");
                        store = mSession.getStore(url);
                    } else {
                        // 4)  auth failed.  show error and retry. 
                        if (mAuthenticator.isShowing()) {
                            mAuthenticator.setErrorMessage(me.getMessage(), me);
                        } else {
                            // 5) must have been some other error.  throw it. 
                            if (nextEx != null) {
                                if (nextEx instanceof java.net.UnknownHostException) {
                                    throw new MessagingException(Pooka.getResources().formatMessage("error.login.unknownHostException", nextEx.getMessage()), me);
                                }
                            }
                            throw me;
                        }
                    }
                }
            }
        }
        getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  connection succeeded; connected = true.");
        connected = true;
        if (useSubscribed && protocol.equalsIgnoreCase("imap")) {
            synchSubscribed();
        }
        if (Pooka.getProperty("Pooka.openFoldersOnConnect", "true").equalsIgnoreCase("true")) {
            for (int i = 0; i < children.size(); i++) {
                doOpenFolders((FolderInfo) children.elementAt(i));
            }
        }
    }
}
