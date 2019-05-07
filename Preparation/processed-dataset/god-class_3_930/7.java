/**
     * @see org.java.plugin.registry.PluginRegistry#unregister(java.lang.String[])
     */
public Collection<String> unregister(final String[] ids) {
    // collecting registered extension points and extensions  
    final List<ExtensionPoint> registeredPoints = new LinkedList<ExtensionPoint>();
    final Map<String, Extension> registeredExtensions = new HashMap<String, Extension>();
    for (PluginDescriptor pluginDescriptor : registeredPlugins.values()) {
        for (ExtensionPoint point : pluginDescriptor.getExtensionPoints()) {
            registeredPoints.add(point);
            for (Extension ext : point.getConnectedExtensions()) {
                registeredExtensions.put(ext.getUniqueId(), ext);
            }
        }
    }
    final Set<String> result = new HashSet<String>();
    RegistryChangeDataImpl registryChangeData = new RegistryChangeDataImpl();
    // collect objects to be unregistered  
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "unregisteringPrepare", //$NON-NLS-1$  
    null));
    Map<String, PluginDescriptor> removingPlugins = new HashMap<String, PluginDescriptor>();
    Map<String, PluginFragment> removingFragments = new HashMap<String, PluginFragment>();
    for (String element : ids) {
        PluginDescriptor descr = registeredPlugins.get(element);
        if (descr != null) {
            for (PluginDescriptor depDescr : getDependingPlugins(descr)) {
                removingPlugins.put(depDescr.getId(), depDescr);
                registryChangeData.removedPlugins().add(depDescr.getId());
            }
            removingPlugins.put(descr.getId(), descr);
            registryChangeData.removedPlugins().add(descr.getId());
            continue;
        }
        PluginFragment fragment = registeredFragments.get(element);
        if (fragment != null) {
            removingFragments.put(fragment.getId(), fragment);
            continue;
        }
        registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.WARNING, null, IntegrityCheckReport.Error.NO_ERROR, "pluginToUngregisterNotFound", element));
    }
    for (PluginDescriptor descr : removingPlugins.values()) {
        for (PluginFragment fragment : descr.getFragments()) {
            if (removingFragments.containsKey(fragment.getId())) {
                continue;
            }
            removingFragments.put(fragment.getId(), fragment);
        }
    }
    // notify about plug-ins removal first  
    fireEvent(registryChangeData);
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "unregisteringFragmentsStart", null));
    //$NON-NLS-1$  
    for (PluginFragment pluginFragment : removingFragments.values()) {
        PluginFragmentImpl fragment = (PluginFragmentImpl) pluginFragment;
        unregisterFragment(fragment);
        if (!removingPlugins.containsKey(fragment.getPluginId())) {
            registryChangeData.modifiedPlugins().add(fragment.getPluginId());
        }
        result.add(fragment.getUniqueId());
    }
    removingFragments.clear();
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "unregisteringPluginsStart", null));
    //$NON-NLS-1$  
    for (PluginDescriptor pluginDescriptor : removingPlugins.values()) {
        PluginDescriptorImpl descr = (PluginDescriptorImpl) pluginDescriptor;
        unregisterPlugin(descr);
        result.add(descr.getUniqueId());
    }
    removingPlugins.clear();
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "unregisteringPluginsFinish", //$NON-NLS-1$  
    Integer.valueOf(registeredPlugins.size())));
    registrationReport.add(new ReportItemImpl(IntegrityCheckReport.Severity.INFO, null, IntegrityCheckReport.Error.NO_ERROR, "unregisteringFragmentsFinish", //$NON-NLS-1$  
    Integer.valueOf(registeredFragments.size())));
    log.info("plug-in and fragment descriptors unregistered - " + result.size());
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
        for (PluginDescriptor descriptor : registeredPlugins.values()) {
            for (ExtensionPoint point : descriptor.getExtensionPoints()) {
                for (Extension ext : point.getConnectedExtensions()) {
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
