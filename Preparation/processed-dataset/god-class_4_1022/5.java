/**
   * this is called by loadFolders if a proper Folder object
   * is returned.
   */
protected void initializeFolderInfo() {
    addFolderListeners();
    Pooka.getResources().addValueChangeListener(this, getFolderProperty());
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".folderList");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".defaultProfile");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".displayFilters");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".backendFilters");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".notifyNewMessagesMain");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".notifyNewMessagesNode");
    Pooka.getLogManager().addLogger(getFolderProperty());
    String defProfile = Pooka.getProperty(getFolderProperty() + ".defaultProfile", "");
    if ((!defProfile.equals("")) && (!defProfile.equals(UserProfile.S_DEFAULT_PROFILE_KEY)))
        defaultProfile = Pooka.getPookaManager().getUserProfileManager().getProfile(defProfile);
    // if we got to this point, we should assume that the open worked. 
    if (getFolderTracker() == null) {
        FolderTracker tracker = Pooka.getFolderTracker();
        if (tracker != null) {
            tracker.addFolder(this);
            this.setFolderTracker(tracker);
        } else {
            if (Pooka.sStartupManager.isShuttingDown()) {
                getLogger().fine("No FolderTracker available.");
            } else {
                getLogger().warning("Error:  No FolderTracker available for folder " + getFolderID());
            }
        }
    }
}
