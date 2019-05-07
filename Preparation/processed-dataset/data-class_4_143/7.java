/**
   * Returns a named action.
   * @param action The action
   */
public static MenuAction getAction(String action) {
    Object o = actionHash.get(action);
    if (o == null)
        o = pythonActionHash.get(action);
    return (MenuAction) o;
}
