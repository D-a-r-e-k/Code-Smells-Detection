//}}}  
//{{{ propertiesChanged() method  
private void propertiesChanged() {
    showHiddenFiles = jEdit.getBooleanProperty("vfs.browser.showHiddenFiles");
    sortMixFilesAndDirs = jEdit.getBooleanProperty("vfs.browser.sortMixFilesAndDirs");
    sortIgnoreCase = jEdit.getBooleanProperty("vfs.browser.sortIgnoreCase");
    doubleClickClose = jEdit.getBooleanProperty("vfs.browser.doubleClickClose");
    browserView.propertiesChanged();
    toolbarBox.removeAll();
    if (jEdit.getBooleanProperty("vfs.browser.showToolbar")) {
        Container toolbar = createToolBar();
        if (horizontalLayout)
            toolbarBox.add(toolbar);
        else {
            toolbarBox.add(toolbar);
        }
    }
    if (jEdit.getBooleanProperty("vfs.browser.showMenubar")) {
        Container menubar = createMenuBar();
        if (horizontalLayout) {
            toolbarBox.add(menubar, 0);
        } else {
            menubar.add(Box.createGlue());
            toolbarBox.add(menubar);
        }
    } else {
        plugins = null;
        favorites = null;
    }
    revalidate();
    if (path != null)
        reloadDirectory();
}
