/**
     * @see org.java.plugin.registry.PluginRegistry#registerListener(
     *      org.java.plugin.registry.PluginRegistry.RegistryChangeListener)
     */
public void registerListener(final RegistryChangeListener listener) {
    if (listeners.contains(listener)) {
        throw new IllegalArgumentException("listener " + listener + " already registered");
    }
    listeners.add(listener);
}
