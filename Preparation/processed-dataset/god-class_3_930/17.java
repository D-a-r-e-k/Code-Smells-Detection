/**
     * @see org.java.plugin.registry.PluginRegistry#getPluginDescriptors()
     */
public Collection<PluginDescriptor> getPluginDescriptors() {
    final Collection<PluginDescriptor> empty_collection = Collections.emptyList();
    return registeredPlugins.isEmpty() ? empty_collection : Collections.unmodifiableCollection(registeredPlugins.values());
}
