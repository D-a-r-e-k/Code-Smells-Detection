/**
   * Add a python action listener to the list.
   * @param name Internal action name
   * @param script The python source script
   * @param editAction True if this is an edit action
   */
public static void addPythonAction(String name, String script, boolean editAction) {
    PythonAction action;
    if (!editAction)
        action = new PythonAction(name, script);
    else
        action = new PythonEditAction(name, script);
    pythonActionHash.put(name, action);
    String keyStroke = getProperty(name.concat(".shortcut"));
    if (keyStroke != null)
        inputHandler.addKeyBinding(keyStroke, action);
}
