private void initCustomComponents() {
    // Since a custom menu can't be added using Netbeans form editor,  
    // We add it just after the initComponents.  
    recentMenu = new com.finalist.jaggenerator.menu.RecentMenu();
    recentMenu.setMnemonic(KeyEvent.VK_R);
    recentMenu.setText("Recent...");
    recentMenu.setMainApp(this);
    fileMenu.insert(recentMenu, 2);
}
