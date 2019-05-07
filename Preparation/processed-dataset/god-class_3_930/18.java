/**
     * @see org.java.plugin.registry.PluginRegistry#getPluginFragments()
     */
public Collection<PluginFragment> getPluginFragments() {
    final Collection<PluginFragment> empty_collection = Collections.emptyList();
    return registeredFragments.isEmpty() ? empty_collection : Collections.unmodifiableCollection(registeredFragments.values());
}
