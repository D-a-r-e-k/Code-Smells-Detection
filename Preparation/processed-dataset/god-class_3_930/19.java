/**
     * @see org.java.plugin.registry.PluginRegistry#getDependingPlugins(
     *      org.java.plugin.registry.PluginDescriptor)
     */
public Collection<PluginDescriptor> getDependingPlugins(final PluginDescriptor descr) {
    Map<String, PluginDescriptor> result = new HashMap<String, PluginDescriptor>();
    for (PluginDescriptor dependedDescr : getPluginDescriptors()) {
        if (dependedDescr.getId().equals(descr.getId())) {
            continue;
        }
        for (PluginPrerequisite pre : dependedDescr.getPrerequisites()) {
            if (!pre.getPluginId().equals(descr.getId()) || !pre.matches()) {
                continue;
            }
            if (!result.containsKey(dependedDescr.getId())) {
                result.put(dependedDescr.getId(), dependedDescr);
                for (PluginDescriptor descriptor : getDependingPlugins(dependedDescr)) {
                    if (!result.containsKey(descriptor.getId())) {
                        result.put(descriptor.getId(), descriptor);
                    }
                }
            }
            break;
        }
    }
    return result.values();
}
