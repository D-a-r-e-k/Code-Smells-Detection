private void unregisterFragment(final PluginFragmentImpl fragment) {
    PluginDescriptorImpl descr = (PluginDescriptorImpl) registeredPlugins.get(fragment.getPluginId());
    if (descr != null) {
        descr.unregisterFragment(fragment);
    }
    registeredFragments.remove(fragment.getId());
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "fragmentUnregistered", fragment.getUniqueId()));
}
