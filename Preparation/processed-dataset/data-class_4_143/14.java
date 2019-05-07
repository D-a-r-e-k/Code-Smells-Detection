/**
   * Returns a plugin by it's class name.
   * @param name The plugin to return
   */
public static Plugin getPlugin(String name) {
    for (int i = 0; i < plugins.size(); i++) {
        Plugin p = (Plugin) plugins.get(i);
        if (p.getClass().getName().equalsIgnoreCase(name))
            return p;
    }
    return null;
}
