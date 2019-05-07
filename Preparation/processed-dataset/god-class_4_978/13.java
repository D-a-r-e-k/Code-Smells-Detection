/**
   * Registers a plugin with the editor. This will also call
   * the <code>start()</code> method of the plugin.
   */
public static void addPlugin(Plugin plugin) {
    plugins.add(plugin);
    try {
        plugin.start();
    } catch (Throwable t) {
        System.err.println("#--An exception has occurred while starting plugin:");
        t.printStackTrace();
    }
    if (plugin instanceof SkinFactory) {
        //System.out.println("Added a SkinPlugin named: " + plugin.getClass().getName()); 
        SkinManager.registerSkinFactory((SkinFactory) plugin);
    }
}
