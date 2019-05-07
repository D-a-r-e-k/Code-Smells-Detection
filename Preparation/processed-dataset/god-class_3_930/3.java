/**
     * General algorithm:
     * <ol>
     *   <li>Collect all currently registered extension points.</li>
     *   <li>Parse given URL's as XML content files and separate them on plug-in
     *       and plug-in fragment descriptors.</li>
     *   <li>Process new plug-in descriptors first:
     *     <ol>
     *       <li>Instantiate new PluginDescriptorImpl object.</li>
     *       <li>Handle versions correctly - register new descriptor as most
     *           recent version or as an old version.</li>
     *       <li>If other versions of the same plug-in already registered, take
     *           their fragments and register them with this version.</li>
     *     </ol>
     *   </li>
     *   <li>Process new plug-in fragments next:
     *     <ol>
     *       <li>Instantiate new PluginFragmentImpl object.</li>
     *       <li>Check if older version of the same fragment already registered.
     *           If yes, un-register it and move to old plug-in fragments
     *           collection.</li>
     *       <li>Register new fragment with all matches plug-in descriptors (if
     *           this fragment is of most recent version).</li>
     *      </ol>
     *    </li>
     *    <li>Notify collected extension points about potential changes in
     *        extensions set.</li>
     *    <li>Propagate events about registry changes.</li>
     * </ol>
     * @see org.java.plugin.registry.PluginRegistry#register(java.net.URL[])
     */
public Map<String, Identity> register(final URL[] manifests) throws ManifestProcessingException {
    // collecting registered extension points and extensions  
    List<ExtensionPoint> registeredPoints = new LinkedList<ExtensionPoint>();
    Map<String, Extension> registeredExtensions = new HashMap<String, Extension>();
    for (PluginDescriptor descriptor : registeredPlugins.values()) {
        for (ExtensionPoint point : descriptor.getExtensionPoints()) {
            registeredPoints.add(point);
            for (Extension ext : point.getConnectedExtensions()) {
                registeredExtensions.put(ext.getUniqueId(), ext);
            }
        }
    }
    Map<String, Identity> result = new HashMap<String, Identity>(manifests.length);
    Map<String, ModelPluginManifest> plugins = new HashMap<String, ModelPluginManifest>();
    Map<String, ModelPluginManifest> fragments = new HashMap<String, ModelPluginManifest>();
    // parsing given manifests  
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "manifestsParsingStart", //$NON-NLS-1$  
    null));
    for (URL url : manifests) {
        ModelPluginManifest model;
        try {
            model = manifestParser.parseManifest(url);
        } catch (Exception e) {
            log.error("can't parse manifest file " + url, e);
            //$NON-NLS-1$  
            if (stopOnError) {
                throw new ManifestProcessingException(PACKAGE_NAME, "manifestParsingError", url, e);
            }
            registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.ERROR, null, IntegrityCheckReport.Error.MANIFEST_PROCESSING_FAILED, "manifestParsingError", new Object[] { url, e }));
            //$NON-NLS-1$  
            continue;
        }
        if (model instanceof ModelPluginFragment) {
            fragments.put(url.toExternalForm(), model);
            continue;
        }
        if (!(model instanceof ModelPluginDescriptor)) {
            log.warn("URL " + url + " points to XML document of unknown type");
            //$NON-NLS-1$  
            continue;
        }
        plugins.put(url.toExternalForm(), model);
    }
    if (log.isDebugEnabled()) {
        log.debug("manifest files parsed, plugins.size=" + plugins.size() + ", fragments.size=" + fragments.size());
    }
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "manifestsParsingFinish", //$NON-NLS-1$  
    new Object[] { Integer.valueOf(plugins.size()), Integer.valueOf(fragments.size()) }));
    checkVersions(plugins);
    if (log.isDebugEnabled()) {
        log.debug("plug-ins versions checked, plugins.size=" + plugins.size());
    }
    checkVersions(fragments);
    if (log.isDebugEnabled()) {
        log.debug("plug-in fragments versions checked, fragments.size=" + fragments.size());
    }
    RegistryChangeDataImpl registryChangeData = new RegistryChangeDataImpl();
    // registering new plug-ins  
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "registeringPluginsStart", null));
    //$NON-NLS-1$  
    for (ModelPluginManifest model : plugins.values()) {
        PluginDescriptor descr = registerPlugin((ModelPluginDescriptor) model, registryChangeData);
        if (descr != null) {
            result.put(descr.getLocation().toExternalForm(), descr);
        }
    }
    plugins.clear();
    // registering new plug-in fragments  
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "registeringFragmentsStart", null));
    //$NON-NLS-1$  
    for (ModelPluginManifest entry : fragments.values()) {
        PluginFragment fragment = registerFragment((ModelPluginFragment) entry, registryChangeData);
        if (fragment != null) {
            result.put(fragment.getLocation().toExternalForm(), fragment);
        }
    }
    fragments.clear();
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "registeringPluginsFinish", //$NON-NLS-1$  
    Integer.valueOf(registeredPlugins.size())));
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "registeringFragmentsFinish", //$NON-NLS-1$  
    Integer.valueOf(registeredFragments.size())));
    log.info("plug-in and fragment descriptors registered - " + result.size());
    dump();
    if (result.isEmpty()) {
        return result;
    }
    // notify all interested members that plug-ins set has been changed  
    for (ExtensionPoint extensionPoint : registeredPoints) {
        ((ExtensionPointImpl) extensionPoint).registryChanged();
    }
    for (Extension extension : registeredExtensions.values()) {
        ((ExtensionImpl) extension).registryChanged();
    }
    if (!listeners.isEmpty() || log.isDebugEnabled()) {
        // analyze changes in extensions set  
        for (PluginDescriptor pluginDescriptor : registeredPlugins.values()) {
            for (ExtensionPoint extensionPoint : pluginDescriptor.getExtensionPoints()) {
                for (Extension ext : extensionPoint.getConnectedExtensions()) {
                    if (!registeredExtensions.containsKey(ext.getUniqueId())) {
                        registryChangeData.putAddedExtension(ext.getUniqueId(), makeUniqueId(ext.getExtendedPluginId(), ext.getExtendedPointId()));
                    } else {
                        registeredExtensions.remove(ext.getUniqueId());
                        if (registryChangeData.modifiedPlugins().contains(ext.getDeclaringPluginDescriptor().getId()) || registryChangeData.modifiedPlugins().contains(ext.getExtendedPluginId())) {
                            registryChangeData.putModifiedExtension(ext.getUniqueId(), makeUniqueId(ext.getExtendedPluginId(), ext.getExtendedPointId()));
                        }
                    }
                }
            }
        }
        for (Extension ext : registeredExtensions.values()) {
            registryChangeData.putRemovedExtension(ext.getUniqueId(), makeUniqueId(ext.getExtendedPluginId(), ext.getExtendedPointId()));
        }
        // fire event  
        fireEvent(registryChangeData);
    }
    return result;
}
