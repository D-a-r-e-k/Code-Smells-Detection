private void checkVersions(final Map<String, ModelPluginManifest> plugins) throws ManifestProcessingException {
    Map<String, Object[]> versions = new HashMap<String, Object[]>();
    //<ID, [Version, URL]>  
    Set<String> toBeRemovedUrls = new HashSet<String>();
    for (Iterator<Map.Entry<String, ModelPluginManifest>> it = plugins.entrySet().iterator(); it.hasNext(); ) {
        Map.Entry<String, ModelPluginManifest> entry = it.next();
        String url = entry.getKey();
        ModelPluginManifest model = entry.getValue();
        if (registeredPlugins.containsKey(model.getId())) {
            if (stopOnError) {
                throw new ManifestProcessingException(PACKAGE_NAME, "duplicatePlugin", //$NON-NLS-1$  
                model.getId());
            }
            it.remove();
            registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.ERROR, null, IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED, "duplicatedPluginId", model.getId()));
            //$NON-NLS-1$  
            continue;
        }
        if (registeredFragments.containsKey(model.getId())) {
            if (stopOnError) {
                throw new ManifestProcessingException(PACKAGE_NAME, "duplicatePluginFragment", //$NON-NLS-1$  
                model.getId());
            }
            it.remove();
            registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.ERROR, null, IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED, "duplicatedFragmentId", model.getId()));
            //$NON-NLS-1$  
            continue;
        }
        Object[] version = versions.get(model.getId());
        if (version == null) {
            versions.put(model.getId(), new Object[] { model.getVersion(), url });
            continue;
        }
        if (((Version) version[0]).compareTo(model.getVersion()) < 0) {
            toBeRemovedUrls.add((String) version[1]);
            versions.put(model.getId(), new Object[] { model.getVersion(), url });
        } else {
            toBeRemovedUrls.add(url);
        }
    }
    versions.clear();
    for (String url : toBeRemovedUrls) {
        plugins.remove(url);
    }
    toBeRemovedUrls.clear();
}
