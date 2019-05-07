protected void removeFromListeners(FolderDisplayUI display) {
    if (display != null) {
        removeMessageChangedListener(display);
        removeMessageCountListener(display);
    }
}
