//}}}  
//{{{ createMenuBar() method  
private Container createMenuBar() {
    JToolBar menuBar = new JToolBar();
    menuBar.setFloatable(false);
    menuBar.add(new CommandsMenuButton());
    menuBar.add(Box.createHorizontalStrut(3));
    menuBar.add(plugins = new PluginsMenuButton());
    menuBar.add(Box.createHorizontalStrut(3));
    menuBar.add(favorites = new FavoritesMenuButton());
    return menuBar;
}
