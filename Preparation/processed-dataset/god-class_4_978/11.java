/**
   * Makes each mode know what plugins to start when it is selected.
   */
public static void assocPluginsToModes() {
    Mode mode;
    String modeName;
    String pluginModes;
    for (int i = 0; i < plugins.size(); i++) {
        Plugin plugin = (Plugin) plugins.get(i);
        pluginModes = getProperty("plugin." + plugin.getClass().getName() + ".modes");
        if (pluginModes != null) {
            StringTokenizer tok = new StringTokenizer(pluginModes);
            while (tok.hasMoreTokens()) {
                modeName = tok.nextToken();
                mode = getMode(modeName);
                mode.addPlugin(plugin);
            }
        }
    }
}
