/**
     * @see org.java.plugin.registry.PluginRegistry#isExtensionPointAvailable(
     *      java.lang.String)
     */
public boolean isExtensionPointAvailable(final String uniqueId) {
    return isExtensionPointAvailable(extractPluginId(uniqueId), extractId(uniqueId));
}
