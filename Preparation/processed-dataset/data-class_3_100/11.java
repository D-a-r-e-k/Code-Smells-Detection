/**
     * @see org.java.plugin.registry.PluginRegistry#getExtensionPoint(
     *      java.lang.String, java.lang.String)
     */
public ExtensionPoint getExtensionPoint(final String pluginId, final String pointId) {
    PluginDescriptor descriptor = registeredPlugins.get(pluginId);
    if (descriptor == null) {
        throw new IllegalArgumentException("unknown plug-in ID " + pluginId + " provided for extension point " + pointId);
    }
    for (ExtensionPoint point : descriptor.getExtensionPoints()) {
        if (point.getId().equals(pointId)) {
            if (point.isValid()) {
                return point;
            }
            log.warn("extension point " + point.getUniqueId() + " is invalid and ignored by registry");
            //$NON-NLS-1$  
            break;
        }
    }
    throw new IllegalArgumentException("unknown extension point ID - " + makeUniqueId(pluginId, pointId));
}
