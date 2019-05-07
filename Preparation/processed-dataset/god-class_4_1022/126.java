/**
   * This sets the trashFolder value.  it also resets the defaultAction
   * list and erases the FolderNode's popupMenu, if there is one.
   */
public void setTrashFolder(boolean newValue) {
    trashFolder = newValue;
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
    resetDefaultActions();
    if (getFolderNode() != null)
        getFolderNode().popupMenu = null;
}
