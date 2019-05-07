/**
   * Add an action listener to the list.
   * @param action The action listener
   */
public static void addAction(MenuAction action) {
    String name = action.getName();
    actionHash.put(name, action);
    String keyStroke = getProperty(name.concat(".shortcut"));
    if (keyStroke != null)
        inputHandler.addKeyBinding(keyStroke, action);
}
