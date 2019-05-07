/**
   * This handles the changes if the source property is modified.
   *
   * As defined in net.suberic.util.ValueChangeListener.
   */
public void valueChanged(String pChangedValue) {
    final String changedValue = pChangedValue;
    javax.swing.AbstractAction valueChangedAction = new javax.swing.AbstractAction() {

        public void actionPerformed(java.awt.event.ActionEvent ae) {
            // check to make sure that we still exist. 
            List<String> storeList = Pooka.getResources().getPropertyAsList("Store", "");
            if (storeList.contains(getStoreID())) {
                if (changedValue.equals(getStoreProperty() + ".folderList")) {
                    updateChildren();
                } else if (changedValue.equals(getStoreProperty() + ".defaultProfile")) {
                    String defProfileString = Pooka.getProperty(getStoreProperty() + ".defaultProfile", "");
                    if (defProfileString.length() < 1 || defProfileString.equalsIgnoreCase(UserProfile.S_DEFAULT_PROFILE_KEY)) {
                        defaultProfile = null;
                    } else {
                        defaultProfile = Pooka.getPookaManager().getUserProfileManager().getProfile(defProfileString);
                    }
                } else if (changedValue.equals(getStoreProperty() + ".protocol") || changedValue.equals(getStoreProperty() + ".user") || changedValue.equals(getStoreProperty() + ".password") || changedValue.equals(getStoreProperty() + ".server") || changedValue.equals(getStoreProperty() + ".port") || changedValue.equals(getStoreProperty() + ".SSL")) {
                    if (storeNode != null) {
                        Enumeration childEnum = storeNode.children();
                        Vector v = new Vector();
                        while (childEnum.hasMoreElements()) v.add(childEnum.nextElement());
                        storeNode.removeChildren(v);
                    }
                    children = null;
                    try {
                        disconnectStore();
                    } catch (Exception e) {
                    }
                    getLogger().log(Level.FINE, "calling configureStore()");
                    configureStore();
                } else if (changedValue.equals(getStoreProperty() + ".connection")) {
                    connection.removeConnectionListener(StoreInfo.this);
                    connection = Pooka.getConnectionManager().getConnection(Pooka.getProperty(getStoreProperty() + ".connection", ""));
                    if (connection == null) {
                        connection = Pooka.getConnectionManager().getDefaultConnection();
                    }
                    if (connection != null) {
                        connection.addConnectionListener(StoreInfo.this);
                    }
                } else if (changedValue.equals(getStoreProperty() + ".useSubscribed")) {
                    useSubscribed = Pooka.getProperty(getStoreProperty() + ".useSubscribed", "false").equalsIgnoreCase("true");
                } else if (changedValue.equals(getStoreProperty() + ".sessionDebug.level")) {
                    updateSessionDebug();
                }
            }
        }
    };
    // if we don't do the update synchronously on the store thread, 
    // then subscribing to subfolders breaks. 
    java.awt.event.ActionEvent actionEvent = new java.awt.event.ActionEvent(this, 0, "value-changed");
    if (Thread.currentThread() == getStoreThread()) {
        valueChangedAction.actionPerformed(actionEvent);
    } else {
        getStoreThread().addToQueue(valueChangedAction, actionEvent);
    }
}
