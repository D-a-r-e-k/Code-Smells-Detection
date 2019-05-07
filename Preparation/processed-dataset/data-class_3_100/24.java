/**
     * @see org.java.plugin.registry.PluginRegistry#makeUniqueId(
     *      java.lang.String, org.java.plugin.registry.Version)
     */
public String makeUniqueId(final String pluginId, final Version version) {
    return pluginId + UNIQUE_SEPARATOR + version;
}
