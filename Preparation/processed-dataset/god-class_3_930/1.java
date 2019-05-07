/**
     * @see org.java.plugin.registry.PluginRegistry#configure(
     *      ExtendedProperties)
     */
public void configure(final ExtendedProperties config) {
    stopOnError = "true".equalsIgnoreCase(//$NON-NLS-1$  
    config.getProperty("stopOnError", "false"));
    //$NON-NLS-1$ //$NON-NLS-2$  
    boolean isValidating = !"false".equalsIgnoreCase(//$NON-NLS-1$  
    config.getProperty("isValidating", "true"));
    //$NON-NLS-1$ //$NON-NLS-2$  
    manifestParser = new ManifestParser(isValidating);
    log.info("configured, stopOnError=" + stopOnError + ", isValidating=" + isValidating);
}
