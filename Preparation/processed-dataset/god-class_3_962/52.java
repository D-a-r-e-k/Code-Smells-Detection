protected JMenu createLogLevelColorMenu() {
    JMenu colorMenu = new JMenu("Configure LogLevel Colors");
    colorMenu.setMnemonic('c');
    Iterator levels = getLogLevels();
    while (levels.hasNext()) {
        colorMenu.add(createSubMenuItem((LogLevel) levels.next()));
    }
    return colorMenu;
}
