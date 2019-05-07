private PluginDescriptor registerPlugin(final ModelPluginDescriptor model, final RegistryChangeDataImpl registryChangeData) throws ManifestProcessingException {
    if (log.isDebugEnabled()) {
        log.debug("registering plug-in, URL - " + model.getLocation());
    }
    PluginDescriptorImpl result = null;
    try {
        result = new PluginDescriptorImpl(this, model);
        registryChangeData.addedPlugins().add(result.getId());
        // applying fragments to the new plug-in  
        for (PluginFragment pluginFragment : registeredFragments.values()) {
            PluginFragmentImpl fragment = (PluginFragmentImpl) pluginFragment;
            if (fragment.matches(result)) {
                result.registerFragment(fragment);
            }
        }
        registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "pluginRegistered", result.getUniqueId()));
    } catch (ManifestProcessingException mpe) {
        log.error("failed registering plug-in, URL - " + model.getLocation(), mpe);
        if (stopOnError) {
            throw mpe;
        }
        registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.ERROR, null, IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED, "pluginRegistrationFailed", //$NON-NLS-1$  
        new Object[] { model.getLocation(), mpe }));
        return null;
    }
    registeredPlugins.put(result.getId(), result);
    return result;
}
