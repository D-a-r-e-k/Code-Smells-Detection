/**
   * Stop plugins.
   */
/* friendly */
static void stopPlugins() {
    Plugin[] plugins = getPlugins();
    for (int i = 0; i < plugins.length; i++) try {
        plugins[i].stop();
    } catch (Throwable t) {
        System.err.println("#--An exception has occurred while stopping plugin:");
        t.printStackTrace();
    }
}
