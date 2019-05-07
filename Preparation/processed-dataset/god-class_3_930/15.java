/**
     * @see org.java.plugin.registry.PluginRegistry#getPluginDescriptor(java.lang.String)
     */
public PluginDescriptor getPluginDescriptor(final String pluginId) {
    PluginDescriptor result = registeredPlugins.get(pluginId);
    if (result == null) {
        throw new IllegalArgumentException("unknown plug-in ID - " + pluginId);
    }
    return result;
}
