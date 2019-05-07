/**
   * This sets the given FolderDisplayUI to be the UI for this
   * FolderInfo.
   *
   * It automatically registers that FolderDisplayUI to be a listener
   * to MessageCount, MessageChanged, and Connection events.
   */
public void setFolderDisplayUI(FolderDisplayUI newValue) {
    removeFromListeners(folderDisplayUI);
    folderDisplayUI = newValue;
    addToListeners(folderDisplayUI);
}
