/**
   * Loads all plugins in a directory.
   * @param directory The directory
   */
public static void loadPlugins(String directory) {
    String[] args = { directory };
    System.out.println(getProperty("jar.scanningdir", args));
    File file = new File(directory);
    if (!(file.exists() || file.isDirectory()))
        return;
    String[] plugins = file.list();
    if (plugins == null)
        return;
    for (int i = 0; i < plugins.length; i++) {
        String plugin = plugins[i];
        if (!plugin.toLowerCase().endsWith(".jar"))
            continue;
        try {
            new JARClassLoader(directory + File.separator + plugin);
        } catch (IOException io) {
            String[] args2 = { plugin };
            System.err.println(getProperty("jar.error.load", args2));
            io.printStackTrace();
        }
    }
}
