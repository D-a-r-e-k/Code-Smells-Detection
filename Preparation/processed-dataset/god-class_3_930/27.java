/**
     * @see org.java.plugin.registry.PluginRegistry#extractVersion(java.lang.String)
     */
public Version extractVersion(final String uniqueId) {
    int p = uniqueId.indexOf(UNIQUE_SEPARATOR);
    if ((p <= 0) || (p >= (uniqueId.length() - 1))) {
        throw new IllegalArgumentException("invalid unique ID - " + uniqueId);
    }
    return Version.parse(uniqueId.substring(p + 1));
}
