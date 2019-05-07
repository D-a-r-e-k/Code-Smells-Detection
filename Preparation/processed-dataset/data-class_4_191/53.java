/**
   * This handles the changes if the source property is modified.
   *
   * As defined in net.suberic.util.ValueChangeListener.
   */
public void valueChanged(String changedValue) {
    if (changedValue.equals(getFolderProperty() + ".folderList")) {
        final Runnable runMe = new Runnable() {

            public void run() {
                ((javax.swing.tree.DefaultTreeModel) (((FolderPanel) folderNode.getParentContainer()).getFolderTree().getModel())).nodeStructureChanged(folderNode);
            }
        };
        // if we don't do the update synchronously on the folder thread, 
        // then subscribing to subfolders breaks. 
        if (Thread.currentThread() != getFolderThread()) {
            getFolderThread().addToQueue(new javax.swing.AbstractAction() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    updateChildren();
                    if (folderNode != null) {
                        javax.swing.SwingUtilities.invokeLater(runMe);
                    }
                }
            }, new java.awt.event.ActionEvent(this, 0, "open-all"));
        } else {
            updateChildren();
            if (folderNode != null) {
                javax.swing.SwingUtilities.invokeLater(runMe);
            }
        }
    } else if (changedValue.equals(getFolderProperty() + ".defaultProfile")) {
        String newProfileValue = Pooka.getProperty(changedValue, "");
        if (newProfileValue.length() < 1 || newProfileValue.equals(UserProfile.S_DEFAULT_PROFILE_KEY))
            defaultProfile = null;
        else
            defaultProfile = Pooka.getPookaManager().getUserProfileManager().getProfile(newProfileValue);
    } else if (changedValue.equals(getFolderProperty() + ".backendFilters")) {
        createFilters();
    } else if (changedValue.equals(getFolderProperty() + ".displayFilters")) {
        createFilters();
        unloadMatchingFilters();
    } else if (changedValue.equals(getFolderProperty() + ".notifyNewMessagesMain") || changedValue.equals(getFolderProperty() + ".notifyNewMessagesNode")) {
        setNotifyNewMessagesMain(!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"));
        setNotifyNewMessagesNode(!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false"));
    }
}
