//}}}  
//{{{ createPluginsMenu() method  
public JComponent createPluginsMenu(JComponent pluginMenu, boolean showManagerOptions) {
    ActionHandler actionHandler = new ActionHandler();
    if (showManagerOptions && getMode() == BROWSER) {
        pluginMenu.add(GUIUtilities.loadMenuItem("plugin-manager", false));
        pluginMenu.add(GUIUtilities.loadMenuItem("plugin-options", false));
        if (pluginMenu instanceof JMenu)
            ((JMenu) pluginMenu).addSeparator();
        else if (pluginMenu instanceof JPopupMenu)
            ((JPopupMenu) pluginMenu).addSeparator();
    } else
        /* we're in a modal dialog */
        ;
    List<JMenuItem> vec = new ArrayList<JMenuItem>();
    //{{{ old API  
    Enumeration<VFS> e = VFSManager.getFilesystems();
    while (e.hasMoreElements()) {
        VFS vfs = e.nextElement();
        if ((vfs.getCapabilities() & VFS.BROWSE_CAP) == 0)
            continue;
        JMenuItem menuItem = new JMenuItem(jEdit.getProperty("vfs." + vfs.getName() + ".label"));
        menuItem.setActionCommand(vfs.getName());
        menuItem.addActionListener(actionHandler);
        vec.add(menuItem);
    }
    //}}}  
    //{{{ new API  
    EditPlugin[] plugins = jEdit.getPlugins();
    for (int i = 0; i < plugins.length; i++) {
        JMenuItem menuItem = plugins[i].createBrowserMenuItems();
        if (menuItem != null)
            vec.add(menuItem);
    }
    //}}}  
    if (!vec.isEmpty()) {
        Collections.sort(vec, new MenuItemTextComparator());
        for (int i = 0; i < vec.size(); i++) pluginMenu.add(vec.get(i));
    } else {
        JMenuItem mi = new JMenuItem(jEdit.getProperty("vfs.browser.plugins.no-plugins.label"));
        mi.setEnabled(false);
        pluginMenu.add(mi);
    }
    return pluginMenu;
}
