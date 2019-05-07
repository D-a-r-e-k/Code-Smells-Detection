/**
     * @see org.java.plugin.registry.PluginRegistry#isExtensionPointAvailable(
     *      java.lang.String, java.lang.String)
     */
public boolean isExtensionPointAvailable(final String pluginId, final String pointId) {
    PluginDescriptor descriptor = registeredPlugins.get(pluginId);
    if (descriptor == null) {
        return false;
    }
    for (ExtensionPoint point : descriptor.getExtensionPoints()) {
        if (point.getId().equals(pointId)) {
            return point.isValid();
        }
    }
    return false;
}
