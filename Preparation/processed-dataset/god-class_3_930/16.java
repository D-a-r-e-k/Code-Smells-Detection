/**
     * @see org.java.plugin.registry.PluginRegistry#isPluginDescriptorAvailable(java.lang.String)
     */
public boolean isPluginDescriptorAvailable(final String pluginId) {
    return registeredPlugins.containsKey(pluginId);
}
