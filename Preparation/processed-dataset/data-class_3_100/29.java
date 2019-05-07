/**
     * @see org.java.plugin.registry.PluginRegistry#unregisterListener(
     *      org.java.plugin.registry.PluginRegistry.RegistryChangeListener)
     */
public void unregisterListener(final RegistryChangeListener listener) {
    if (!listeners.remove(listener)) {
        log.warn("unknown listener " + listener);
    }
}
