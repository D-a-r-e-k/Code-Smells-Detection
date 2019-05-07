public void setSentFolder(boolean newValue) {
    sentFolder = newValue;
    if (newValue) {
        setNotifyNewMessagesMain(false);
        setNotifyNewMessagesNode(false);
    } else {
        if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"))
            setNotifyNewMessagesMain(true);
        else
            setNotifyNewMessagesMain(false);
        if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false"))
            setNotifyNewMessagesNode(true);
        else
            setNotifyNewMessagesNode(false);
    }
    setTracksUnreadMessages(!newValue);
    createFilters();
}
