/**
     * @see org.java.plugin.registry.PluginRegistry#extractPluginId(java.lang.String)
     */
public String extractPluginId(final String uniqueId) {
    int p = uniqueId.indexOf(UNIQUE_SEPARATOR);
    if ((p <= 0) || (p >= (uniqueId.length() - 1))) {
        throw new IllegalArgumentException("invalid unique ID - " + uniqueId);
    }
    return uniqueId.substring(0, p);
}
