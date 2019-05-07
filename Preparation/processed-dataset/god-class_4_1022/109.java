protected void addToListeners(FolderDisplayUI display) {
    if (display != null) {
        addMessageChangedListener(display);
        addMessageCountListener(display);
    }
}
