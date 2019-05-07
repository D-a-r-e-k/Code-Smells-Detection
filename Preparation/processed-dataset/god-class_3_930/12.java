/**
     * @see org.java.plugin.registry.PluginRegistry#getExtensionPoint(java.lang.String)
     */
public ExtensionPoint getExtensionPoint(final String uniqueId) {
    return getExtensionPoint(extractPluginId(uniqueId), extractId(uniqueId));
}
