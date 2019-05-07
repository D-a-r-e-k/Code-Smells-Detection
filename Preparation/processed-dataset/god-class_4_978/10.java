/**
   * Loads plugins.
   */
private static void initPlugins() {
    plugins = new ArrayList();
    loadPlugins(JEXT_HOME + File.separator + "plugins");
    loadPlugins(SETTINGS_DIRECTORY + "plugins");
}
