// changes some default UI settings such as trees leaf icons or font size and style... 
private static void initUI() {
    /*if (getBooleanProperty("useJextTheme"))
      MetalLookAndFeel.setCurrentTheme(new JextMetalTheme());*/
    SkinManager.applySelectedSkin();
    // check if menus are flat or not 
    flatMenus = getBooleanProperty("flatMenus");
    // check if buttons are highlighted or not 
    buttonsHighlight = getBooleanProperty("buttonsHighlight");
    // rollover 
    JextButton.setRollover(getBooleanProperty("toolbarRollover"));
}
