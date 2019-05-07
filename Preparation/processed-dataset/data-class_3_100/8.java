private void unregisterPlugin(final PluginDescriptorImpl descr) {
    registeredPlugins.remove(descr.getId());
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "pluginUnregistered", descr.getUniqueId()));
}
