/**
     * @see org.java.plugin.registry.PluginRegistry#checkIntegrity(
     *      org.java.plugin.PathResolver)
     */
public IntegrityCheckReport checkIntegrity(final PathResolver pathResolver) {
    return checkIntegrity(pathResolver, false);
}
