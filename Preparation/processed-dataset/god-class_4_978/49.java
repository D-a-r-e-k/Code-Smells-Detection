/**
   * Returns true if the property value equals to "on" or "true"
   * @param name The name of the property to read
   */
public static boolean getBooleanProperty(String name, String def) {
    String p = getProperty(name, def);
    if (p == null)
        return false;
    else
        return p.equals("on") || p.equals("true");
}
