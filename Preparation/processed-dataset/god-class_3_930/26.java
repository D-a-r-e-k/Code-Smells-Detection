/**
     * @see org.java.plugin.registry.PluginRegistry#extractId(java.lang.String)
     */
public String extractId(final String uniqueId) {
    int p = uniqueId.indexOf(UNIQUE_SEPARATOR);
    if ((p <= 0) || (p >= (uniqueId.length() - 1))) {
        throw new IllegalArgumentException("invalid unique ID - " + uniqueId);
    }
    return uniqueId.substring(p + 1);
}
