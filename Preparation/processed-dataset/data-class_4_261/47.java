//}}}  
//{{{ createToolBar() method  
private Container createToolBar() {
    if (mode == BROWSER)
        return GUIUtilities.loadToolBar(actionContext, "vfs.browser.toolbar-browser");
    else
        return GUIUtilities.loadToolBar(actionContext, "vfs.browser.toolbar-dialog");
}
