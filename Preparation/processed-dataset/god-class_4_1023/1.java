/**
   * This configures the store from the property information.
   */
public void configureStore() {
    connected = false;
    available = false;
    protocol = Pooka.getProperty("Store." + storeID + ".protocol", "");
    if (protocol.equalsIgnoreCase("pop3")) {
        user = "";
        password = "";
        server = "localhost";
        if (Pooka.getProperty(getStoreProperty() + ".useMaildir", "unset").equalsIgnoreCase("true"))
            protocol = "maildir";
        else
            protocol = "mbox";
        port = -1;
        popStore = true;
    } else {
        popStore = false;
        user = Pooka.getProperty("Store." + storeID + ".user", "");
        password = Pooka.getProperty("Store." + storeID + ".password", "");
        String portValue = Pooka.getProperty("Store." + storeID + ".port", "");
        port = -1;
        if (!portValue.equals("")) {
            try {
                port = Integer.parseInt(portValue);
            } catch (Exception e) {
            }
        }
        if (!password.equals(""))
            password = net.suberic.util.gui.propedit.PasswordEditorPane.descrambleString(password);
        server = Pooka.getProperty("Store." + storeID + ".server", "");
        sslSetting = Pooka.getProperty(getStoreProperty() + ".SSL", "none");
        if (sslSetting.equalsIgnoreCase("true")) {
            Pooka.setProperty(getStoreProperty() + ".SSL", "ssl");
            sslSetting = "ssl";
        } else if (sslSetting.equalsIgnoreCase("false")) {
            Pooka.setProperty(getStoreProperty() + ".SSL", "none");
            sslSetting = "none";
        }
        if (sslSetting.equals("ssl")) {
            if (protocol.equals("imap"))
                protocol = "imaps";
        }
    }
    Properties p = loadProperties();
    if (protocol.equalsIgnoreCase("maildir")) {
        url = new URLName(protocol, server, port, p.getProperty("mail.store.maildir.baseDir"), user, password);
    } else {
        url = new URLName(protocol, server, port, "", user, password);
    }
    getLogger().fine("creating authenticator");
    mAuthenticator = Pooka.getUIFactory().createAuthenticatorUI();
    try {
        mSession = Session.getInstance(p, mAuthenticator);
        updateSessionDebug();
        store = mSession.getStore(url);
        available = true;
    } catch (NoSuchProviderException nspe) {
        Pooka.getUIFactory().showError(Pooka.getProperty("error.loadingStore", "Unable to load Store ") + getStoreID(), nspe);
        available = false;
    }
    // don't allow a StoreInfo to get created with an empty folderList. 
    if (Pooka.getProperty("Store." + storeID + ".folderList", "").equals(""))
        Pooka.setProperty("Store." + storeID + ".folderList", "INBOX");
    // check to see if we're using the subscribed property. 
    useSubscribed = Pooka.getProperty(getStoreProperty() + ".useSubscribed", "false").equalsIgnoreCase("true");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty());
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".folderList");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".defaultProfile");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".protocol");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".user");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".password");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".server");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".port");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".connection");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".SSL");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".useSubscribed");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".sessionDebug.level");
    // tell the log manager to listen to these settings. 
    Pooka.getLogManager().addLogger(getStoreProperty());
    Pooka.getLogManager().addLogger(getStoreProperty() + ".sessionDebug");
    if (available) {
        connectionListener = new ConnectionListener() {

            public void disconnected(ConnectionEvent e) {
                getLogger().log(Level.FINE, "Store " + getStoreID() + " disconnected.");
                // if we think we're connected, then call disconnectStore. 
                if (isConnected()) {
                    try {
                        if (Pooka.getUIFactory().getMessageNotificationManager() != null) {
                            Pooka.getUIFactory().getMessageNotificationManager().displayMessage("Disconnected", "Disconnected from store " + getStoreID(), net.suberic.pooka.gui.MessageNotificationManager.WARNING_MESSAGE_TYPE);
                        }
                        disconnectStore();
                    } catch (MessagingException me) {
                        getLogger().log(Level.FINE, "error disconnecting Store:  " + me.getMessage());
                    }
                }
            }

            public void closed(ConnectionEvent e) {
                getLogger().log(Level.FINE, "Store " + getStoreID() + " closed.");
                // if we think we're connected, then call disconnectStore. 
                if (isConnected()) {
                    if (Pooka.getUIFactory().getMessageNotificationManager() != null) {
                        Pooka.getUIFactory().getMessageNotificationManager().displayMessage("Disconnected", "Disconnected from store " + getStoreID(), net.suberic.pooka.gui.MessageNotificationManager.WARNING_MESSAGE_TYPE);
                    }
                    try {
                        disconnectStore();
                    } catch (MessagingException me) {
                        getLogger().log(Level.FINE, "error disconnecting Store:  " + me.getMessage());
                    }
                }
            }

            public void opened(ConnectionEvent e) {
                getLogger().log(Level.FINE, "Store " + getStoreID() + " opened.");
            }
        };
        store.addConnectionListener(connectionListener);
    }
    if (storeThread == null) {
        storeThread = new ActionThread(this.getStoreID() + " - ActionThread");
        storeThread.start();
    }
    String defProfileString = Pooka.getProperty(getStoreProperty() + ".defaultProfile", "");
    if (defProfileString.length() < 1 || defProfileString.equalsIgnoreCase(UserProfile.S_DEFAULT_PROFILE_KEY)) {
        defaultProfile = null;
    } else {
        defaultProfile = Pooka.getPookaManager().getUserProfileManager().getProfile(defProfileString);
    }
    connection = Pooka.getConnectionManager().getConnection(Pooka.getProperty(getStoreProperty() + ".connection", ""));
    if (connection == null) {
        connection = Pooka.getConnectionManager().getDefaultConnection();
    }
    if (connection != null) {
        connection.addConnectionListener(this);
    }
    updateChildren();
    String trashFolderName = Pooka.getProperty(getStoreProperty() + ".trashFolder", "");
    if (trashFolderName.length() > 0) {
        trashFolder = getChild(trashFolderName);
        if (trashFolder != null)
            trashFolder.setTrashFolder(true);
    }
}
