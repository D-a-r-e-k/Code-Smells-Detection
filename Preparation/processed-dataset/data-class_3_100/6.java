private PluginFragment registerFragment(final ModelPluginFragment model, final RegistryChangeDataImpl registryChangeData) throws ManifestProcessingException {
    if (log.isDebugEnabled()) {
        log.debug("registering plug-in fragment descriptor, URL - " + model.getLocation());
    }
    PluginFragmentImpl result = null;
    try {
        result = new PluginFragmentImpl(this, model);
        // register fragment with all matches plug-ins  
        boolean isRegistered = false;
        PluginDescriptorImpl descr = (PluginDescriptorImpl) getPluginDescriptor(result.getPluginId());
        if (result.matches(descr)) {
            descr.registerFragment(result);
            if (!registryChangeData.addedPlugins().contains(descr.getId())) {
                registryChangeData.modifiedPlugins().add(descr.getId());
            }
            isRegistered = true;
        }
        if (!isRegistered) {
            log.warn("no matching plug-ins found for fragment " + result.getUniqueId());
            registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.WARNING, null, IntegrityCheckReport.Error.NO_ERROR, "noMatchingPluginFound", result.getUniqueId()));
        }
        registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "fragmentRegistered", result.getUniqueId()));
    } catch (ManifestProcessingException mpe) {
        log.error("failed registering plug-in fragment descriptor, URL - " + model.getLocation(), mpe);
        if (stopOnError) {
            throw mpe;
        }
        registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.ERROR, null, IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED, "fragmentRegistrationFailed", //$NON-NLS-1$  
        new Object[] { model.getLocation(), mpe }));
        return null;
    }
    registeredFragments.put(result.getId(), result);
    return result;
}
