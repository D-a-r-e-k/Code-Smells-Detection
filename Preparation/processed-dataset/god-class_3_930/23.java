/**
     * @see org.java.plugin.registry.PluginRegistry#makeUniqueId(
     *      java.lang.String, java.lang.String)
     */
public String makeUniqueId(final String pluginId, final String id) {
    return pluginId + UNIQUE_SEPARATOR + id;
}
