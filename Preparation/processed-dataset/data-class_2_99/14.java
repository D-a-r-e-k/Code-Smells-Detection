private static Set<String> writeDescripor(final PluginRegistry registry, final Filter filter, final ObjectOutputStream strm) throws IOException {
    final Map<String, ArchiveDescriptorEntry> result = new HashMap<String, ArchiveDescriptorEntry>();
    for (PluginDescriptor descr : registry.getPluginDescriptors()) {
        if (!filter.accept(descr.getId(), descr.getVersion(), false)) {
            continue;
        }
        result.put(descr.getUniqueId(), new ArchiveDescriptorEntry(descr.getId(), descr.getVersion(), false, Util.readUrlContent(descr.getLocation())));
    }
    for (PluginFragment fragment : registry.getPluginFragments()) {
        if (!filter.accept(fragment.getId(), fragment.getVersion(), true)) {
            continue;
        }
        result.put(fragment.getUniqueId(), new ArchiveDescriptorEntry(fragment.getId(), fragment.getVersion(), true, Util.readUrlContent(fragment.getLocation())));
    }
    strm.writeObject(result.values().toArray(new ArchiveDescriptorEntry[result.size()]));
    return result.keySet();
}
